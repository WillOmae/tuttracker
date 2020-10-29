package dev.wilburomae.tuttracker.views.listeners;

import android.net.Uri;

import dev.wilburomae.tuttracker.models.Assignment;

public interface IUploadListener {
    void upload(Assignment assignment, Uri fileUri);
}
