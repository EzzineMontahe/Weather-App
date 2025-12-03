package com.montahe.weatherapp;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView cityText, tempText, descriptionText, humidityText, windText;
    private EditText cityInput;
    private Button searchBtn;
    private ProgressBar loading;
    private ImageView weatherIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupViews();
        loadWeather("Tunis"); // Start with default city
    }

    private void setupViews() {
        cityText = findViewById(R.id.cityText);
        tempText = findViewById(R.id.tempText);
        descriptionText = findViewById(R.id.descriptionText);
        humidityText = findViewById(R.id.humidityText);
        windText = findViewById(R.id.windText);
        cityInput = findViewById(R.id.cityInput);
        searchBtn = findViewById(R.id.searchBtn);
        loading = findViewById(R.id.loading);
        weatherIcon = findViewById(R.id.weatherIcon);

        searchBtn.setOnClickListener(v -> {
            String city = cityInput.getText().toString();
            if (!city.isEmpty()) {
                loadWeather(city);
            } else {
                Toast.makeText(this, "Please enter a city name", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadWeather(String city) {
        loading.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherApi api = retrofit.create(WeatherApi.class);
        Call<WeatherData> call = api.getWeather(city, "e5ef726f75c84b3dc2515d7236206018", "metric");

        call.enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                loading.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    displayWeather(response.body());
                } else {
                    showError("City not found");
                }
            }

            @Override
            public void onFailure(Call<WeatherData> call, Throwable t) {
                loading.setVisibility(View.GONE);
                showError("Check internet connection");
            }
        });
    }

    private void displayWeather(WeatherData data) {
        cityText.setText(data.cityName);
        tempText.setText(String.format("%.1f°C", data.main.temperature));
        descriptionText.setText(data.weather[0].description);
        humidityText.setText(data.main.humidity + "%");
        windText.setText(String.format("%.1f km/h", data.wind.speed));

        // Load dynamic weather icon
        String iconUrl = "https://openweathermap.org/img/wn/" + data.weather[0].icon + "@2x.png";
        Glide.with(this).load(iconUrl).into(weatherIcon);
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}