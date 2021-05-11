<p align="center">
  <img alt="Screenshot" width="250" src="https://user-images.githubusercontent.com/7033377/117814363-80a98880-b264-11eb-9c73-b3091c95f5bc.png">
  
  <br>
  
  <a href="https://play.google.com/store/apps/details?id=com.phoqe.fackla&pcampaignid=pcampaignidMKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1">
    <img alt="Get it on Google Play" width="150" src="https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png">
  </a>
</p>

# Fackla

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

## Contributing

Use **Issues** for any questions, problems, bugs, or feature requests. If you’ve already fixed something, submit a **Pull Request**. There is no style guide or anything resembling a Contributors Guide. Include what you deem necessary.

## Future Improvements

### Geocoding

When the Mapbox Search SDK for Android is out of beta, the notification content text could include the physical address for the virtual location’s coordinates.
Including the Search SDK will require changing the minimum SDK version to 21. The current state of the SDK doesn’t yield accurate results, but that might change.

### Google Assistant App Actions

Would be cool to integrate Google Assistant in some way. Needs more research.

### Widget

## Attribution

Google Play and the Google Play logo are trademarks of Google LLC.

Illustrations by [unDraw](https://undraw.co).

## License

MIT
