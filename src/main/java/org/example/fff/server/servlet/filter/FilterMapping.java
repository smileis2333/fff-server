package org.example.fff.server.servlet.filter;

import org.example.fff.server.util.FilterMappingType;

public class FilterMapping {
    private FilterMappingType filterMappingType;

    private String URLPattern;

    private String servletName;

    private String filterName;

    private FilterMapping() {

    }

    public static FilterMapping newUrlPatternMapping(FilterMappingType filterMappingType, String filterName, String URLPattern) {
        FilterMapping filterMapping = new FilterMapping();
        filterMapping.URLPattern = URLPattern;
        filterMapping.filterMappingType = filterMappingType;
        filterMapping.filterName = filterName;
        return filterMapping;
    }

    public static FilterMapping newServletMapping(FilterMappingType filterMappingType, String filterName, String servletName) {
        FilterMapping filterMapping = new FilterMapping();
        filterMapping.servletName = servletName;
        filterMapping.filterMappingType = filterMappingType;
        filterMapping.filterName = filterName;
        return filterMapping;
    }

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

