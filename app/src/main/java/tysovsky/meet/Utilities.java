package tysovsky.meet;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * Created by tysovsky on 4/3/16.
 */
public class Utilities {

    public static String[] PERMISSIONS_LOCATION = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    public static int LOCATIONS_REQUEST_CODE = 0;

    public static boolean isMarshmallow(){
        return (Build.VERSION.SDK_INT> Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static boolean hasPermissions(String[] permissions, Context context){
        if(isMarshmallow()){

            for(int i = 0; i < permissions.length; i++){
                if(context.checkSelfPermission(permissions[i]) != PackageManager.PERMISSION_GRANTED){
                    return false;
                }
            }
            return true;

        }

        return true;

    }
}
