# Fackla

Fackla is an open-source Android app written in Kotlin featuring mock locations with an intuitive UI from Mapbox.
The project is more of a personal introduction to designing and building apps with Material Design and Kotlin than a real product.
Feel free to interact with the project as per the open-source license.

<a href="https://play.google.com/store/apps/details?id=com.phoqe.fackla&pcampaignid=pcampaignidMKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1">
  <img alt="Get it on Google Play" width="150" src="https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png">
</a>

## Screenshots

<p>
  <img width="200" src="https://user-images.githubusercontent.com/7033377/117584365-cbb08800-b10c-11eb-97f2-64541ce39c2e.png">
  <img width="200" src="https://user-images.githubusercontent.com/7033377/117584368-cc491e80-b10c-11eb-9fd1-ff021c6a3d68.png">
</p>

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
- `FOREGROUND_SERVICE`: The app starts a foreground service when the user changed their location.
- `ACCESS_BOOT_COMPLETED`: Prior to device reboot, if the user was running the service it will start again. 

## Building

### Credentials

You’ll need a `cred.properties` file at the root of the project to build Fackla, it should looking something like:

```
STORE_FILE=STORE_FILE
STORE_PASS=STORE_PASS

KEY_ALIAS=KEY_ALIAS
KEY_PASS=KEY_PASS

MAPBOX_DOWNLOAD_TOKEN=MAPBOX_DOWNLOAD_TOKEN
```

Fackla will use these properties when signing the AAB and APK (mainly for publishing the app via Google Play Store etc.).

### Mapbox

Visit the [Mapbox Installation Guide](https://docs.mapbox.com/android/maps/guides/install) for further instructions and to get a secret access token.
You’ll need to add your secret access token in the `cred.properties` file.
It’s read from the project-level `build.gradle` file. Make sure to keep it **secret**.

## Usage

Long press a point on the map to change your location to the point’s coordinates.

## Contributing

Use **Issues** for any questions, problems, bugs, or feature requests. If you’ve already fixed something, submit a **Pull Request**. There is no style guide or anything resembling a Contributors Guide. Include what you deem necessary.

## Governance

Fackla isn’t a big project. Mapbox, Google Play Developer, and Firebase is controlled by [@phoqe](https://github.com/phoqe). Everything else is in this repository.

## Future Improvements

### Geocoding

When the Mapbox Search SDK for Android is out of beta, the notification content text could include the physical address for the virtual location’s coordinates.
Including the Search SDK will require changing the minimum SDK version to 21. The current state of the SDK doesn’t yield accurate results, but that might change.

### Google Assistant App Actions

Would be cool to integrate Google Assistant in some way. Needs more research.

### Widget

## Attribution

Illustrations by [unDraw](https://undraw.co).

## License

MIT
