# MioVini
![Preview-Screens](https://github.com/andre-ceccon/MioVini/blob/master/image_readme.png)

## About this Project
The idea of the App is:
_" You can write down and classify all the wines you try, to get a specific idea of your preferences and thus better manage your Wine House."_

## Why?
This project is part of my personal portfolio, so, I'll be happy if you could provide me any feedback about the project, code, structure or anything that you can report that could make me a better developer!

Email-me: dekorodrigues@gmail.com

Connect with me at [LinkedIn](https://linkedin.com/in/andre-ceccon).

## Installers
If you want to test the App in the Production mode, the installers are listed below:

[Android .apk installer](https://play.google.com/store/apps/details?id=vinho.andre.android.com.gerenciadorvinho).

iOS. ipa installer: Soon!

## Functionalities
- Register the details of your wines such as name, country of origin, producer, amount in the Wine House, ideal temperature, grapes, harmonization, personal comments, rating, vintage, image of the wine bottle, store you bought, price paid and date of purchase.
- The visualization of the entries first is through a list with only some information, if you want to see all the information just choose an item from the list that will open the details screen.
- The available filters are: Wine House, Classification, Favorites, Name and Country of Origin.
- Search
	- The search is performed through the fields: Name, Comment, Country and Harmonization.
	- The search is done based on the local database ([SQLITE](https://developer.android.com/reference/android/database/sqlite/package-summary)), it is filled every 6 hours, only if connected to a WI-FI, with information from the Cloud Firestore. To do this synchronization task I use [JobService](https://developer.android.com/reference/android/app/job/JobService).
- Settings
	- Modify the profile name
	- Update your email address
	- Update your account password
	- Set the filter preference for when the application is opened.

## Getting Started

### Prerequisites
To run this project in development mode, you will need a basic environment to run Android Studio and SDK Tools, which can be found [here](https://developer.android.com/about).

### Installing
**Cloning the Repository**

$ git clone https://github.com/andre-ceccon/MioVini.git

## Built With
- [Kotlin](https://kotlinlang.org/) - Build the native app using Kotlin.

## Support tools
- [Firebase](https://firebase.google.com/) – Cloud Firestore and Storage Service.
- [Mask EditText](https://github.com/santalu/mask-edittext) – Mask for EditText.
- [AndroidUtilCode](https://firebase.google.com/) – This library encapsulates the functions that commonly used in Android development.
- [ImageUtil Compression Library](https://firebase.google.com/) – Compressor is a lightweight and powerful android image compression library.
- [ImageUtil Gallery e Camera API](https://firebase.google.com/) – A simple library that allows you to select images from the device library or directly from the camera.
- [RoundedImageView](https://github.com/vinc3m1/RoundedImageView) – A fast ImageView (and Drawable) that supports rounded corners (and ovals or circles) based on the original 
- [PhotoView](https://github.com/chrisbanes/PhotoView) – PhotoView aims to help produce an easily usable implementation of a zooming Android ImageView.

## Contributing
You can send how many PR's do you want. I'll be glad to analyse and accept them! And if you have any question about the project.

Email-me: dekorodrigues@gmail.com

Connect with me at [LinkedIn](https://linkedin.com/in/andre-ceccon)

Thank you!

## License
This project is licensed under the Apache License 2.0 - see the [LICENSE.md](https://github.com/andre-ceccon/MioVini/blob/master/LICENSE) file for details.
