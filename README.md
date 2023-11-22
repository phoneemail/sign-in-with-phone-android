# Sign-in with Phone Android Project

Welcome to the Sign-in with Phone Android project! This project demonstrates a seamless user authentication flow in an Android app using JSON Web Tokens (JWT) and the Activity Result API.

## Getting Started

Follow the steps below to incorporate the "Login with Phone" feature into your Android app:

1. **Acquire the API Key:**
   Obtain the necessary API key from [Phone Email App](https://play.google.com/store/apps/details?id=email.phone.app)  to enable secure communication with the authentication service.

2. **Implementing the Login Button:**
   In your app's login screen, implement the login button click listener to launch the `AuthActivity` using the Activity Result API.

3. **Handling Results with the Launcher:**
   Register an activity result launcher to handle the result of the `AuthActivity`, extracting the JWT from the result data.

4. **Implementing AuthActivity:**
   AuthActivity is responsible for verifying the phone number through a web-based authentication process. Upon successful verification, the JWT is obtained from a JavaScript interface and set as the result.
   Replace the placeholder mobile details in the AuthActivity (PHONE_COUNTRY and PHONE_NUMBER) with your registered mobile information.

6. **Managing the JWT in the Calling Activity:**
   Handle the JWT obtained from the `AuthActivity` within the calling activity's `launcher` callback, enabling further actions based on the JWT.

7. **Conclusion:**
   Customize the authentication logic and error handling based on your app's requirements.

## Project Structure

The project is organized as follows:

    - `app`: Contains the main Android application code.
    - `MainActivity`: Entry point of the app with the login button implementation.
    - `AuthActivity`: Manages the phone number verification through a WebView and JavaScript interface.
    - Other relevant files and resources.

## Dependencies

- AndroidX
- WebView
- nimbus-jose-jwt

## Usage

Clone the repository and open the project in Android Studio. Follow the steps outlined in the "Getting Started" section to integrate the "Login with Phone" feature into your app.

## Contributing

Feel free to contribute to the project by opening issues or submitting pull requests. Your feedback and suggestions are highly appreciated.

## License

This project is licensed under the [MIT License](LICENSE).

---

Happy coding!

