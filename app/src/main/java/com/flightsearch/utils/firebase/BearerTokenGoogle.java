package com.flightsearch.utils.firebase;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.flightsearch.constants.ApplicationConstants;
import com.google.auth.oauth2.GoogleCredentials;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BearerTokenGoogle implements ApplicationConstants {

    public static final String  firebaseMessagingScope = "https://www.googleapis.com/auth/firebase.messaging";
    public static Context context;

    public static String getAccessToken() {
        try {
            String jsonString = "{\n" +
                    "  \"type\": \"service_account\",\n" +
                    "  \"project_id\": \"masters-362af\",\n" +
                    "  \"private_key_id\": \"2b4da373b20549c3ef783aa9df45093de77ce167\",\n" +
                    "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDamGL6A4Jmojau\\n7z25cDWlq6vxJFoE1xeAoExoW1M60vAJr5vSeEsgXa/UWu/ALDTWP1BJbL9uIMJQ\\n+atcwVSOl5qVB9TIpQKYmRndaJfGTbDj+il4X5j4dSJUbPfx63C3h7ttXurOlW38\\n8OLDvWApo8UsSFFAHE7V2LX4L+ZSXLTKzK4k40Uujt9yiZjP2erL5pDMKOt/gllI\\n8Wy0r6TN+AqbCrstq7ktB3F+ZlqtTnY80pzYt/97+kb3uwQU3cAD479H7CwYKCzd\\nJgNYmqBmkzIeDGQfOzfAUZHCfYbfAnBXe4TglkmrDYuKKdKay4cRuI/nVx2cj0la\\nlenGKWTRAgMBAAECggEAIZcWaSzDi68+5UAberS6HwHI+NPehq02uKvpPIVWviH5\\nLhkvbKF83zWw9doeH/2rBjEnI+FIw/eCD0djrxaA6S3KselI0qbomze2OHEAwZbh\\nU7xL8GoK65he9MePN5GM+ebVmkeM9Wm+Q7FlvIZhPITPS7AL1uTYCGxgqglSRIge\\n5OHw+tTJreHsKF9EIn8hA4zJdWJ23IWzBT7AHOgP6TrvI5m9l3Tde5ZltPkmMd+M\\nBNJy3z8KHvJuxxTSfkV/mUmauzoiMuOuqoxoZif7JX/nqXE4zJpDzTjlfAx8wFt4\\nffgnK5zdB4Upj0sHpUTlU9uisZQzTOP82IFa0XMqMQKBgQD7SW4bN54G+gzjcdMC\\nBbai1Taqe3Cr9rW8LRFJ+XvOdRENcyiOb/L49fmHqYSOYhd5EJwLJQTL3VVqdN/h\\nrlQUxC59m/zUfCgdQnxjzrFoHo5rhczs3wmJcMOEexNIBfEQd/VEpBO/0k/j89LG\\nPOiP6ZNgxyqVUX0h6CsQeRPq/wKBgQDesfxevp8dzXJgERk9tKr4cstcprXV4RQS\\n0KduQWI3lvUUTwIskzljTPPq91A2Gew9J6R+nExvSkSpKE1IWcs9ZV4MGScYXxRB\\ninN0KEDWDaTK93eSJmQJbi1aDu6DQhC1fHhkrZzbjg0KV58ZgovCKV/1ea6G14uE\\ngb/Nzr7ALwKBgEtweRDMurGHgjUKJ/n0cychcX7u/h1yPI8YzJbzwjpyJMNv7h4M\\n99nMJrSWrMf+JOPgm6gw3ebCNPF30vqy1mVBnF9zZAz6lSRroGJqXBJREhqvmZ0H\\nPJq5cskkFd7Kgdua19Ramd89qWRa/80p3fvOeMNWJ6+aPkHerIcOgm9LAoGBAKl/\\n21DZ0g5DA10vZoDa9I7qAPNiSGCkUj0H54g55+Hb2mo8wLDg1ftI5RbgaoLjNDZP\\n6BoeKOdEJgKClGAPSGxQrUaUFnesVqSUFtBAmyjRda6usKni4p1y6L31Q4FQVZtt\\nQ82Nfyh1dGN80bH+9RUxnMIgfcBQavbOMwkY5YMtAoGAMJZGGmTG+MG9uSJ7dqu3\\nlhA7nJ4BSVpGQqR1Aegc7cPs5FikQHKGXN96OcXtaHoORTGsQ4SNwT8IHMNTyYw1\\n9Ri25VFLI1mIt22KeJRdD0YnePyKvrXD9J8ftJ0NSdAPsoa+XASu3VCHvGk44I4O\\nHuSo30FEU58hYJSFvhRWNI4=\\n-----END PRIVATE KEY-----\\n\",\n" +
                    "  \"client_email\": \"firebase-adminsdk-f0ton@masters-362af.iam.gserviceaccount.com\",\n" +
                    "  \"client_id\": \"104237415465056325227\",\n" +
                    "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
                    "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
                    "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
                    "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-f0ton%40masters-362af.iam.gserviceaccount.com\",\n" +
                    "  \"universe_domain\": \"googleapis.com\"\n" +
                    "}";
            InputStream stream = new ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8));
            ArrayList<String> list = new ArrayList<>();
            list.add(firebaseMessagingScope);
            GoogleCredentials googleCredentials = GoogleCredentials.fromStream(stream).createScoped(list);
            googleCredentials.refresh();
            String accessToken = googleCredentials.getAccessToken().getTokenValue();
            System.out.println("ACCESS TOKEN: " + accessToken);
            return accessToken;
        } catch (IOException e) {
            return "";
        }
    }

    public static void sendNotifications(List<String> devices, String title, String body) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        for (String device: devices) {
            new Thread(() -> {
                try {
                    JSONObject messageObject = new JSONObject();
                    JSONObject notificationObject = new JSONObject();
                    JSONObject mainObject = new JSONObject();

                    notificationObject.put("title", title);
                    notificationObject.put("body", body);
                    messageObject.put("token", device);
                    messageObject.put("notification", notificationObject);
                    mainObject.put("message", messageObject);

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, GOOGLE_MESSAGES_URL, mainObject, response -> {

                    }, volleyError -> {

                    }) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            String token = getAccessToken();
                            Map<String, String> header = new HashMap<>();
                            header.put("Authorization", "Bearer " + token);
                            return header;
                        }
                    };
                    requestQueue.add(request);
                } catch (JSONException ignore) {

                }

            }).start();
        }
        requestQueue.start();
    }

}
