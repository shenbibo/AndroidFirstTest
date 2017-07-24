package retrofit.test.weather;

/**
 * [一句话描述类的作用]
 * [详述类的功能。]
 * Created by sky on 2017/6/30.
 */
public class Basic {
    private String city;

    private String cnty;

    private String id;

    private String lat;

    private String lon;

    private String prov;

    public void setCity(String city){
        this.city = city;
    }
    public String getCity(){
        return this.city;
    }
    public void setCnty(String cnty){
        this.cnty = cnty;
    }
    public String getCnty(){
        return this.cnty;
    }
    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return this.id;
    }
    public void setLat(String lat){
        this.lat = lat;
    }
    public String getLat(){
        return this.lat;
    }
    public void setLon(String lon){
        this.lon = lon;
    }
    public String getLon(){
        return this.lon;
    }
    public void setProv(String prov){
        this.prov = prov;
    }
    public String getProv(){
        return this.prov;
    }

    @Override
    public String toString() {
        return "Basic{" +
                "city='" + city + '\'' +
                ", cnty='" + cnty + '\'' +
                ", id='" + id + '\'' +
                ", lat='" + lat + '\'' +
                ", lon='" + lon + '\'' +
                ", prov='" + prov + '\'' +
                '}';
    }
}
