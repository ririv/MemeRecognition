package com.riri.emojirecognition.service;

import java.io.File;
import java.util.Optional;

public interface ClassifyService {
    Optional<String> classify(File image);
}
