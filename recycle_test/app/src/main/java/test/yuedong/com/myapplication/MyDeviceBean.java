package test.yuedong.com.myapplication;


import java.util.LinkedList;


public class MyDeviceBean {


    public LinkedList<DeviceInfo> deviceInfos = new LinkedList<>();


    public static class DeviceInfo {

        public static final int ITEM_TYPE_PHOTO = 1;
        private static final long serialVersionUID = -34964360911147893L;

        public int type;


        public String device_identify;
        public String device_name;
        public String status;
        public String device_appid;
        public String pic_url;


    }


    public interface MultiItemEntity {
        int getItemType();
    }

}
