# Fackla

Fackla is an open-source Android app written in Kotlin featuring mock locations with an intuitive UI from Mapbox.
The project is a personal introduction to designing and building apps with Kotlin and Material Design.
Feel free to interact with the project as per the open-source license.

## Remarks

### Versions

- Minimum SDK: 16
- Target SDK: 30

### Permissions

- `ACCESS_FINE_LOCATION`
- `ACCESS_COARSE_LOCATION`
- `ACCESS_MOCK_LOCATION`
- `ACCESS_BACKGROUND_LOCATION`
- `FOREGROUND_SERVICE`

## Usage

Long press a point on the map to change your location to the point’s coordinates. Fackla uses a foreground service to maintain the mock location in other apps.

## Future Improvements

### Geocoding

When the Mapbox Search SDK for Android is out of beta, the notification content text could include the physical address for the virtual location’s coordinates.
Including the Search SDK will require changing the minimum SDK version to 21. The current state of the SDK doesn’t yield accurate results (or any for that matter), but that might change.

## Attribution

Illustrations by [unDraw](https://undraw.co).

## License

MIT
