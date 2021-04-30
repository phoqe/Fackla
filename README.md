<p align="center">
  <img width="640" src="https://user-images.githubusercontent.com/7033377/116675312-fdf01600-a9a5-11eb-9d0a-3eee8c426d0a.png" alt="Logo Banner">
</p>

Fackla is an open-source Android app written in Kotlin featuring mock locations with an intuitive UI from Mapbox.
The project is more of a personal introduction to designing and building apps with Material Design and Kotlin than a real product.
Feel free to interact with the project as per the open-source license.

## Remarks

### Limitations

#### Developer Options

Navigating to Developer Options in the device’s settings will destroy the running service. This is a limitation of mock location apps. Presumably Android gets rid of them because the user might decide to switch mock location apps.

### Versions

- Minimum SDK: 16
- Target SDK: 30

### Permissions

- `ACCESS_FINE_LOCATION`: Displaying the user’s position on the main map as well as setting test providers for the location manager. 
- `ACCESS_COARSE_LOCATION`: Same as above.
- `ACCESS_MOCK_LOCATION`: Allows the app to function as a mock location app, i.e. can be selected in the Developer Options of the device.
- `ACCESS_BACKGROUND_LOCATION`: Allows the app to use a fake location in the background. 
- `FOREGROUND_SERVICE`: The app starts a foreground service when the user changed their location.
- `ACCESS_BOOT_COMPLETED`: Prior to device reboot, if the user was running the service it will start again. 

## Building

Visit the [Mapbox Installation Guide](https://docs.mapbox.com/android/maps/guides/install). You’ll need a secret access token in your `gradle.properties` file. It’s read from the project-level `build.gradle` file. Make sure to keep it secret.

## Usage

Long press a point on the map to change your location to the point’s coordinates.

## Contributing

Use **Issues** for any questions, problems, bugs, or feature requests. If you’ve already fixed something, submit a **Pull Request**. There is no style guide or anything resembling a Contributors Guide. Include what you deem necessary.

## Governance

Fackla isn’t a big project. The Mapbox account as well as the Google Play Developer account is controlled by [@phoqe](https://github.com/phoqe). Everything else is in this repository.

## Future Improvements

### Geocoding

When the Mapbox Search SDK for Android is out of beta, the notification content text could include the physical address for the virtual location’s coordinates.
Including the Search SDK will require changing the minimum SDK version to 21. The current state of the SDK doesn’t yield accurate results, but that might change.

## Attribution

Illustrations by [unDraw](https://undraw.co).

## License

MIT
