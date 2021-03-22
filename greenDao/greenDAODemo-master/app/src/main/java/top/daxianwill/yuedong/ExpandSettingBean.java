package top.daxianwill.yuedong;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;


/***
 * 1.没有设置自增长的主键
 */

@Entity(nameInDb = "expandSetting")
public class ExpandSettingBean {


    @Id
    @Property(nameInDb = "key")
    private String key;

    @Property(nameInDb = "value")
    private String value;


    @Generated(hash = 1997892837)
    public ExpandSettingBean(String key, String value) {
        this.key = key;
        this.value = value;
    }


    @Generated(hash = 108907232)
    public ExpandSettingBean() {
    }


    public String getKey() {
        return this.key;
    }


    public void setKey(String key) {
        this.key = key;
    }


    public String getValue() {
        return this.value;
    }


    public void setValue(String value) {
        this.value = value;
    }


    @Override
    public String toString() {
        return "ExpandSettingBean{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
