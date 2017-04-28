package pl.kuc_industries.warsawnavihelper;

/**
 * Created by Szymek on 23.04.2017.
 */

public final class Constants {
    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;
    public static final String PACKAGE_NAME =
            "pl.kuc_industries.warsawnavihelper";
    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static final String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";
    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_DATA_EXTRA";

    public static final int TRAM__LINES_PER_ROW = 6;
    public static final int BUS_LINES_PER_ROW = 5;
    // TODO: use ProGuard or other API keys storing method:
    public static final String UM_API_KEY = "b2404019-9b1d-4ff5-9969-af137b28c7da";
}