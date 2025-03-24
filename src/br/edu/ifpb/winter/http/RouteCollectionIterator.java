package br.edu.ifpb.winter.http;

import java.util.Iterator;
import java.util.Map;

public class RouteCollectionIterator implements RouteIterator {
    private final Iterator<Map.Entry<String, Map<String, Route>>> pathIterator;
    private Iterator<Route> methodIterator;

    public RouteCollectionIterator(Map<String, Map<String, Route>> routes) {
        this.pathIterator = routes.entrySet().iterator();
        this.methodIterator = pathIterator.hasNext()
                ? pathIterator.next().getValue().values().iterator()
                : null;
    }

    @Override
    public boolean hasNext() {
        while ((methodIterator == null || !methodIterator.hasNext()) && pathIterator.hasNext()) {
            methodIterator = pathIterator.next().getValue().values().iterator();
        }
        return methodIterator != null && methodIterator.hasNext();
    }

    @Override
    public Route next() {
        if (!hasNext()) {
            throw new IllegalStateException("No more routes available");
        }
        return methodIterator.next();
    }
}
