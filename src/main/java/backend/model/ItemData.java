package backend.model;

public class ItemData {

    Iterable<Sensor> _sensors;
    String _tempHeader;
    String _pressureHeader;
    String _bannerColor;
    String _bannerText;

    public ItemData(Iterable<Sensor> sensors, String tempHeader, String pressureHeader, String bannerColor, String bannerText) {
        _sensors = sensors;
        _tempHeader = tempHeader;
        _pressureHeader = pressureHeader;
        _bannerColor = bannerColor;
        _bannerText = bannerText;
    }

    public Iterable<Sensor> getSensors() {
        return _sensors;
    }

    public String getTempHeader() {
        return _tempHeader;
    }

    public String getPressureHeader() {
        return _pressureHeader;
    }

    public String getBannerColor() {
        return _bannerColor;
    }

    public String getBannerText() {
        return _bannerText;
    }
}
