package com.example.mediarecorderapp;

public class AudioFile {
    String Name,Path;
    Long Length;

    public AudioFile(String name, String path, Long length) {
        Name = name;
        Path = path;
        Length = length;
    }

    public String getName() {
        return Name;
    }

    public String getPath() {
        return Path;
    }

    public Long getLength() {
        return Length;
    }
}
