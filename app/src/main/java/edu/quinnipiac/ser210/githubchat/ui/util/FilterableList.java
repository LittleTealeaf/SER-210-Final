package edu.quinnipiac.ser210.githubchat.ui.util;

import java.util.HashMap;
import java.util.Map;

public class FilterableList<T> {

    private final ChangeListener<T> changeListener;
    private final Filter<T> filter;
    private final Map<T, Boolean> items = new HashMap<>();

    private String filterString = "";

    public FilterableList(Filter<T> filter, ChangeListener<T> changeListener) {
        this.changeListener = changeListener;
        this.filter = filter;
    }

    public void setFilter(String filterString) {
        this.filterString = filterString;
        for (T item : items.keySet()) {
            Boolean current = items.get(item);
            assert current != null;
            Boolean newFilter = filter.filterItem(item, filterString);
            if (newFilter ^ current) {
                if (newFilter) {
                    changeListener.onItemAdded(item);
                } else {
                    changeListener.onItemRemoved(item);
                }
            }
            items.put(item, filter.filterItem(item, filterString));
        }
    }

    public void addItem(T item) {
        if (!items.containsKey(item)) {
            boolean show = filter.filterItem(item, filterString);
            items.put(item, show);
            if (show) {
                changeListener.onItemAdded(item);
            }
        }
    }

    public void removeItem(T item) {
        if (items.getOrDefault(item, false) == Boolean.TRUE) {
            changeListener.onItemRemoved(item);
        }
        items.remove(item);
    }

    public interface Filter<T> {

        boolean filterItem(T item, String filter);
    }

    public interface ChangeListener<T> {

        void onItemAdded(T item);

        void onItemRemoved(T item);
    }
}
