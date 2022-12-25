package top.expli;

public class Permissions {
    public static final int SYSTEM = 0;
    public static final int PAIMON = 1;
    public static final int SU = 2;
    public static final int ADMIN = 3;
    public static final int USER = 4;
    public static final int GUEST = 5;
    public static final int KERNEL = 2;
    public static final int ADMINONLY = 3;
    public static final int PROTECTED = 4;
    public static final int PUBLIC = 5;
    public static String docPermissionToString(int permission){
        switch (permission){
            case KERNEL -> {
                return "P·A·I·M·O·N";
            }
            case ADMINONLY -> {
                return "管理员";
            }
            case PROTECTED -> {
                return "私有";
            }
            case PUBLIC -> {
                return "公开";
            }
            default -> {
                return "其他";
            }
        }
    }
}
