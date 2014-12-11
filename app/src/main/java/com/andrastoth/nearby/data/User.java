package com.andrastoth.nearby.data;

/**
 * Copyright (c) 2014 András Tóth (tothandras). All rights Reserved.
 */
public class User {
    private final String id;
    private final String name;
    private final String picture;

    public static class Builder {
        private String id;
        private String name;
        private String picture;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder picture(String picture) {
            this.picture = picture;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }

    private User(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.picture = builder.picture;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPicture() {
        return picture;
    }
}