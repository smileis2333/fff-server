package org.example.fff.server.servlet.filter;

public class FilterMapping {
    private FilterMappingType filterMappingType;

    private String URLPattern;

    private String servletName;

    private String filterName;

    public boolean supportURLPattern(String URLPattern) {
        //todo
        return filterMappingType == FilterMappingType.URL_PATTERN && this.URLPattern.equals(URLPattern);
    }

    public boolean supportServletName(String servletName) {
        return filterMappingType == FilterMappingType.SERVLET_NAME && this.servletName.equals(servletName);
    }

    public String getFilterName() {
        return filterName;
    }
}

enum FilterMappingType {
    /**
     * url 匹配
     */
    URL_PATTERN,

    /**
     * servlet name匹配
     */
    SERVLET_NAME,
}