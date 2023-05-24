package org.rubisemi.common;

public class AppAuthority {

    public final static String PLACE_ORDER = "PO";
    public final static String VIEW_ORDER = "VO";
    public final static String QUERY_ORDERS = "QO";
    public final static String DELETE_ORDER = "DO";

    private final static String OR = " OR ";
    private final static String AUTH_PREFIX = "hasAuthority('SCOPE_";
    private final static String AUTH_SUFFIX = "')";
    public final static String HAS_ROLE_CUSTOMER = AUTH_PREFIX + AppRole.ROLE_CUSTOMER + AUTH_SUFFIX;
    public final static String HAS_ROLE_ADMIN = AUTH_PREFIX + AppRole.ROLE_ADMIN + AUTH_SUFFIX;;
    public final static String HAS_FUNC_VIEW_ORDER = AUTH_PREFIX  + VIEW_ORDER + AUTH_SUFFIX;

    public final static String CAN_VIEW_ORDER = HAS_ROLE_CUSTOMER + OR + HAS_ROLE_ADMIN + OR + HAS_FUNC_VIEW_ORDER;


    public static boolean hasRoleCustomer(String authority){
        return String.format("SCOPE_%s", AppRole.ROLE_CUSTOMER).equals(authority);
    }
}
