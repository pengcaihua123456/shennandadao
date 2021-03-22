package top.daxianwill.yuedong;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;


/***
 * 1.字段的默认值
 * 2.表名
 * 3.设置主键
 * 4.列名默认大写
 */

@Entity(nameInDb = "setting")
public class SettingBean {

    @Id
    private Long id;

    //    @Id
    @Property(nameInDb = "user_id")
    private int user_id = -1;

    @Property(nameInDb = "islogin")
    private int islogin;

    @Property(nameInDb = "user_rank")
    private int user_rank;

    @Property(nameInDb = "hx_phone")
    private int hx_phone;

    @Property(nameInDb = "user_name")
    private String user_name;

    @Property(nameInDb = "user_password")
    private String user_password;

    @Property(nameInDb = "user_phone")
    private String user_phone;

    @Generated(hash = 1576421483)
    public SettingBean(Long id, int user_id, int islogin, int user_rank,
            int hx_phone, String user_name, String user_password,
            String user_phone) {
        this.id = id;
        this.user_id = user_id;
        this.islogin = islogin;
        this.user_rank = user_rank;
        this.hx_phone = hx_phone;
        this.user_name = user_name;
        this.user_password = user_password;
        this.user_phone = user_phone;
    }

    @Override
    public String toString() {
        return "SettingBean{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", islogin=" + islogin +
                ", user_rank=" + user_rank +
                ", hx_phone=" + hx_phone +
                ", user_name='" + user_name + '\'' +
                ", user_password='" + user_password + '\'' +
                ", user_phone='" + user_phone + '\'' +
                '}';
    }

    @Generated(hash = 1969935259)
    public SettingBean() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getUser_id() {
        return this.user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getIslogin() {
        return this.islogin;
    }

    public void setIslogin(int islogin) {
        this.islogin = islogin;
    }

    public int getUser_rank() {
        return this.user_rank;
    }

    public void setUser_rank(int user_rank) {
        this.user_rank = user_rank;
    }

    public int getHx_phone() {
        return this.hx_phone;
    }

    public void setHx_phone(int hx_phone) {
        this.hx_phone = hx_phone;
    }

    public String getUser_name() {
        return this.user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_password() {
        return this.user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public String getUser_phone() {
        return this.user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }







}
