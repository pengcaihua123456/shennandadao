package flip.example.mac.myapplication;

import android.support.annotation.DrawableRes;

public interface BaseTabEntity {
    String getTabTitle();

    @DrawableRes
    int getTabSelectedIcon();

    @DrawableRes
    int getTabUnselectedIcon();

}
