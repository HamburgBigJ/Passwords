package org.bukkit.configuration.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FileConfiguration implements Serializable {
    private final Map<String, Object> values = new HashMap<>();

    public Object get(String path) {
        return values.get(path);
    }

    public boolean getBoolean(String path) {
        Object value = get(path);
        if (value instanceof Boolean booleanValue) {
            return booleanValue;
        }
        return value != null && Boolean.parseBoolean(value.toString());
    }

    public String getString(String path) {
        Object value = get(path);
        return value != null ? value.toString() : null;
    }

    public int getInt(String path) {
        Object value = get(path);
        if (value instanceof Number number) {
            return number.intValue();
        }
        return value != null ? Integer.parseInt(value.toString()) : 0;
    }

    @SuppressWarnings("unchecked")
    public List<String> getStringList(String path) {
        Object value = get(path);
        if (value instanceof List<?> list) {
            return list.stream().map(Object::toString).collect(Collectors.toCollection(ArrayList::new));
        }
        return new ArrayList<>();
    }

    @SuppressWarnings("unchecked")
    public List<Object> getList(String path, List<Object> defaultValue) {
        Object value = get(path);
        if (value instanceof List<?> list) {
            return new ArrayList<>((List<Object>) list);
        }
        return defaultValue;
    }

    public void set(String path, Object value) {
        if (value == null) {
            values.remove(path);
        } else {
            values.put(path, value);
        }
    }

    public boolean contains(String path) {
        return values.containsKey(path);
    }

    public void save(File file) throws IOException {
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file))) {
            outputStream.writeObject(values);
        }
    }

    @SuppressWarnings("unchecked")
    public void load(File file) throws IOException {
        if (!file.exists()) {
            return;
        }
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))) {
            Object object = inputStream.readObject();
            if (object instanceof Map<?, ?> map) {
                values.clear();
                map.forEach((key, value) -> values.put(String.valueOf(key), value));
            }
        } catch (ClassNotFoundException ignored) {
        }
    }

    Map<String, Object> getValues() {
        return values;
    }
}
