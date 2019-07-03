package com.ljy.misc.net.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Values {
    private final Logger logger = LoggerFactory.getLogger(Values.class);
    private HashMap<String, Object> values;

    public Values() {
        this.values = new HashMap(8);
    }

    public Values(int size) {
        this.values = new HashMap(size, 1.0F);
    }

    public Values(Values from) {
        this.values = new HashMap(from.values);
    }

    public boolean equals(Object object) {
        return !(object instanceof Values) ? false : this.values.equals(((Values)object).values);
    }

    public int hashCode() {
        return this.values.hashCode();
    }

    public void put(String key, String value) {
        this.values.put(key, value);
    }

    public void putAll(Values other) {
        this.values.putAll(other.values);
    }

    public void put(String key, Byte value) {
        this.values.put(key, value);
    }

    public void put(String key, Short value) {
        this.values.put(key, value);
    }

    public void put(String key, Integer value) {
        this.values.put(key, value);
    }

    public void put(String key, Long value) {
        this.values.put(key, value);
    }

    public void put(String key, Float value) {
        this.values.put(key, value);
    }

    public void put(String key, Double value) {
        this.values.put(key, value);
    }

    public void put(String key, Boolean value) {
        this.values.put(key, value);
    }

    public void put(String key, byte[] value) {
        this.values.put(key, value);
    }

    public void putNull(String key) {
        this.values.put(key, (Object)null);
    }

    public int size() {
        return this.values.size();
    }

    public void remove(String key) {
        this.values.remove(key);
    }

    public void clear() {
        this.values.clear();
    }

    public boolean containsKey(String key) {
        return this.values.containsKey(key);
    }

    public Object get(String key) {
        return this.values.get(key);
    }

    public String getAsString(String key) {
        Object value = this.values.get(key);
        return value != null ? value.toString() : null;
    }

    public Long getAsLong(String key) {
        Object value = this.values.get(key);

        try {
            return value != null ? ((Number)value).longValue() : null;
        } catch (ClassCastException var6) {
            if (value instanceof CharSequence) {
                try {
                    return Long.valueOf(value.toString());
                } catch (NumberFormatException var5) {
                    this.logger.error("Cannot parse Long value for " + value + " at key " + key);
                    return null;
                }
            } else {
                this.logger.error("Cannot cast value for " + key + " to a Long: " + value, var6);
                return null;
            }
        }
    }

    public Integer getAsInteger(String key) {
        Object value = this.values.get(key);

        try {
            return value != null ? ((Number)value).intValue() : null;
        } catch (ClassCastException var6) {
            if (value instanceof CharSequence) {
                try {
                    return Integer.valueOf(value.toString());
                } catch (NumberFormatException var5) {
                    this.logger.error("Cannot parse Integer value for " + value + " at key " + key);
                    return null;
                }
            } else {
                this.logger.error("Cannot cast value for " + key + " to a Integer: " + value, var6);
                return null;
            }
        }
    }

    public Short getAsShort(String key) {
        Object value = this.values.get(key);

        try {
            return value != null ? ((Number)value).shortValue() : null;
        } catch (ClassCastException var6) {
            if (value instanceof CharSequence) {
                try {
                    return Short.valueOf(value.toString());
                } catch (NumberFormatException var5) {
                    this.logger.error("Cannot parse Short value for " + value + " at key " + key);
                    return null;
                }
            } else {
                this.logger.error("Cannot cast value for " + key + " to a Short: " + value, var6);
                return null;
            }
        }
    }

    public Byte getAsByte(String key) {
        Object value = this.values.get(key);

        try {
            return value != null ? ((Number)value).byteValue() : null;
        } catch (ClassCastException var6) {
            if (value instanceof CharSequence) {
                try {
                    return Byte.valueOf(value.toString());
                } catch (NumberFormatException var5) {
                    this.logger.error("Cannot parse Byte value for " + value + " at key " + key);
                    return null;
                }
            } else {
                this.logger.error("Cannot cast value for " + key + " to a Byte: " + value, var6);
                return null;
            }
        }
    }

    public Double getAsDouble(String key) {
        Object value = this.values.get(key);

        try {
            return value != null ? ((Number)value).doubleValue() : null;
        } catch (ClassCastException var6) {
            if (value instanceof CharSequence) {
                try {
                    return Double.valueOf(value.toString());
                } catch (NumberFormatException var5) {
                    this.logger.error("Cannot parse Double value for " + value + " at key " + key);
                    return null;
                }
            } else {
                this.logger.error("Cannot cast value for " + key + " to a Double: " + value, var6);
                return null;
            }
        }
    }

    public Float getAsFloat(String key) {
        Object value = this.values.get(key);

        try {
            return value != null ? ((Number)value).floatValue() : null;
        } catch (ClassCastException var6) {
            if (value instanceof CharSequence) {
                try {
                    return Float.valueOf(value.toString());
                } catch (NumberFormatException var5) {
                    this.logger.error("Cannot parse Float value for " + value + " at key " + key);
                    return null;
                }
            } else {
                this.logger.error("Cannot cast value for " + key + " to a Float: " + value, var6);
                return null;
            }
        }
    }

    public Boolean getAsBoolean(String key) {
        Object value = this.values.get(key);

        try {
            return (Boolean)value;
        } catch (ClassCastException var4) {
            if (value instanceof CharSequence) {
                return Boolean.valueOf(value.toString());
            } else if (value instanceof Number) {
                return ((Number)value).intValue() != 0;
            } else {
                this.logger.error("Cannot cast value for " + key + " to a Boolean: " + value, var4);
                return null;
            }
        }
    }

    public byte[] getAsByteArray(String key) {
        Object value = this.values.get(key);
        return value instanceof byte[] ? (byte[])((byte[])value) : null;
    }

    public Set<Map.Entry<String, Object>> valueSet() {
        return this.values.entrySet();
    }

    public Set<String> keySet() {
        return this.values.keySet();
    }

    /** @deprecated */
    @Deprecated
    public void putStringList(String key, List<String> value) {
        this.values.put(key, value);
    }

    /** @deprecated */
    @Deprecated
    public List<String> getStringList(String key) {
        return (List)this.values.get(key);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        String name;
        String value;
        for(Iterator var2 = this.values.keySet().iterator(); var2.hasNext(); sb.append(name + "=" + value)) {
            name = (String)var2.next();
            value = this.getAsString(name);
            if (sb.length() > 0) {
                sb.append(" ");
            }
        }

        return sb.toString();
    }
}