package io.storebackend.api;

import io.storebackend.api.data.StoreObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
public class StoreBackendController {

    private static Logger LOG = LoggerFactory.getLogger(TodosController.class);

    @Value("${store-backend.api.limit:100}")
    long _limit;

    //@Value("${cacheUrl:http://localhost:8888}")
    @Value("${cacheUrl}")
    String _cacheUrl;
    private RestTemplate _cacheTemplate = new RestTemplate();

    // @Value("${backendUrl:http://localhost:9090}")
    @Value("${backendUrl}")
    String _backendUrl;
    private RestTemplate _backendTemplate = new RestTemplate();

    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    private static String DEFAULT_GROUP = "Default group";

    @GetMapping("/")
    public List<StoreObject> retrieve() {
        LOG.debug("Retrieving all StoreObjects");
        StoreObject[] cached = _cacheTemplate.getForEntity(_cacheUrl, StoreObject[].class).getBody();
        //if cache is empty, hydrate
        if(cached.length < 1) {
            LOG.debug("Cache empty, retrieving from backend service");
            Todo[] backendResp = _backendTemplate.getForEntity(_backendUrl, Todo[].class).getBody();
            if(backendResp.length > 0)    Arrays.stream(backendResp)
                    .forEach(e->_cacheTemplate.postForObject(_cacheUrl, e, Todo.class));
            return Arrays.asList(backendResp);
        } else {
            //Return cached list
            return Arrays.asList(cached);
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/")
    public StoreObject create(@RequestBody StoreObject storeObject) {
        // check if cache size is not over the limit
        throwIfOverLimit();

        if(storeObject.getTitle() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "storeObject.title cannot be null on put");
        }

        LOG.debug("Creating TODO: " + storeObject);
        StoreObject obj = new StoreObject();
        if(ObjectUtils.isEmpty(storeObject.getId())) {
            obj.setId(UUID.randomUUID().toString());
        } else {
            obj.setId(storeObject.getId());
        }
        if(!ObjectUtils.isEmpty(storeObject.getTitle())) {
            obj.setTitle(todo.getTitle());
        }
        if(!ObjectUtils.isEmpty(todo.isComplete())) {
            obj.setComplete(todo.isComplete());
        }
        if(ObjectUtils.isEmpty(storeObject.getCategory())) {
            obj.setCategory(DEFAULT_GROUP);
        } else {
            obj.setCategory(storeObject.getCategory());
        }
        if(ObjectUtils.isEmpty(storeObject.getDeadline())) {
            obj.setDeadline(dtf.format(LocalDateTime.now()));
        } else {
            obj.setDeadline(storeObject.getDeadline());
        }

        //Write to DB
        StoreObject saved = _backendTemplate.postForObject(_backendUrl, obj, StoreObject.class);
        LOG.debug("Created in Backend");

        //Invalidate/Add Cache
        StoreObject cached = _cacheTemplate.postForObject(_cacheUrl, saved, StoreObject.class);
        LOG.debug("Created in Cache");
        return saved;
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/{id}")
    public StoreObject put(@PathVariable String id, @RequestBody StoreObject storeObject) {
        if(storeObject.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "storeObject.id cannot be null on put");
        }
        if(!storeObject.getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "storeObject.id ${storeObject.id} and id $id are inconsistent");
        }

        StoreObject obj = new StoreObject();
        obj.setId(storeObject.getId());

        if(!ObjectUtils.isEmpty(storeObject.isComplete())) {
            obj.setComplete(storeObject.isComplete());
        }
        if(ObjectUtils.isEmpty(storeObject.getCategory())) {
            obj.setCategory(DEFAULT_GROUP);
        } else {
            obj.setCategory(todo.getCategory());
        }
        if(ObjectUtils.isEmpty(storeObject.getDeadline())) {
            obj.setDeadline(dtf.format(LocalDateTime.now()));
        } else {
            obj.setDeadline(storeObject.getDeadline());
        }

        //Write to DB
        StoreObject saved = _backendTemplate.postForObject(_backendUrl, obj, StoreObject.class);
        LOG.debug("Created in Backend");

        //Invalidate/Add Cache
        StoreObject cached = _cacheTemplate.postForObject(_cacheUrl, saved, StoreObject.class);
        LOG.debug("Created in Cache");
        return saved;
    }

    @DeleteMapping("/")
    public void deleteAll() {
        LOG.debug("Removing all StoreObjects");

        //Remove from DB
        _backendTemplate.delete(_backendUrl);
        //Remove from Cache
        _cacheTemplate.delete(_cacheUrl);
    }

    @GetMapping("/{id}")
    public StoreObject retrieve(@PathVariable("id") String id) {
        LOG.debug("Retrieving StoreObject: " + id);
        //Check cache + DB
        Todo cached = null;
        try {
            cached = _cacheTemplate.getForEntity(_cacheUrl + "/" + id, StoreObject.class).getBody();
        } catch (HttpStatusCodeException ex) {
            if(ex.getRawStatusCode() != 404) {
                LOG.error("Caching service error downstream", ex);
                throw ex;
            }
        }

        if(cached != null) {
            // found in cache
            LOG.debug("Found cached version");
            return cached;
        } else {
            LOG.debug("Not in cache, retrieving from backend");

            Todo source = null;
            try{
                source = _backendTemplate.getForEntity(_backendUrl + "/" + id, StoreObject.class).getBody();
            } catch (HttpStatusCodeException ex) {
                if(ex.getRawStatusCode() != 404) {
                    LOG.error("Database service error downstream", ex);
                    throw ex;
                }
            }

            if(source != null) {
                LOG.debug("Found in backend");
                cached = _cacheTemplate.postForObject(_cacheUrl, source, StoreObject.class);
                return source;
            }
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "storeObject.id = " + id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") String id){
        //Remove from DB
        _backendTemplate.delete(_backendUrl + "/" + id);

        //Remove from Cache
        _cacheTemplate.delete(_cacheUrl + "/" + id);
    }

    @PatchMapping("/{id}")
    public StoreObject update(@PathVariable("id") String id, @RequestBody StoreObject storeObject) {
        if(storeObject.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "storeObject.id cannot be null on put");
        }
        if(!storeObject.getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "storeObject.id ${todo.id} and id $id are inconsistent");
        }

        StoreObject obj = new StoreObject();
        obj.setId(storeObject.getId());

        if(!ObjectUtils.isEmpty(storeObject.getTitle())) {
            obj.setTitle(storeObject.getTitle());
        }
        if(!ObjectUtils.isEmpty(storeObject.isComplete())) {
            obj.setComplete(storeObject.isComplete());
        }
        if(ObjectUtils.isEmpty(storeObject.getCategory())) {
            obj.setCategory(DEFAULT_GROUP);
        } else {
            obj.setCategory(storeObject.getCategory());
        }
        if(ObjectUtils.isEmpty(storeObject.getDeadline())) {
            obj.setDeadline(dtf.format(LocalDateTime.now()));
        } else {
            obj.setDeadline(storeObject.getDeadline());
        }

        //Write to DB
        StoreObject saved = _backendTemplate.postForObject(_backendUrl, obj, StoreObject.class);
        LOG.debug("Created in Backend");

        //Invalidate/Add Cache
        StoreObject cached = _cacheTemplate.postForObject(_cacheUrl, saved, StoreObject.class);
        LOG.debug("Created in Cache");
        return saved;
    }

    private void throwIfOverLimit() {
        StoreObject[] cached = _cacheTemplate.getForEntity(_cacheUrl, Todo[].class).getBody();
        if(cached.length >= _limit) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "storeObject.api.limit=$limit, storeObject.size=$count");
        } else {
            return;
        }
    }

}
