package dev.wilburomae.tuttracker.views.listeners;

import android.app.Dialog;
import android.net.Uri;

import dev.wilburomae.tuttracker.models.Assignment;

public interface IUploadListener {
    void upload(Dialog dialog, Assignment assignment, Uri fileUri);
}
