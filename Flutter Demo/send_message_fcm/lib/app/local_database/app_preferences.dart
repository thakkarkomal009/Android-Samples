import 'dart:convert';

import 'package:meta/meta.dart';
import 'package:send_message_fcm/app/resource/app_string.dart';
import 'package:shared_preferences/shared_preferences.dart';

class AppPreferences {
  static const String PREF_FIREBASE_TOKEN = "PREF_FIREBASE_TOKEN";
  static const String PREF_IS_LOGGED_IN = "IS_LOGGED_IN";

  // Private variable for SharedPreferences
  SharedPreferences _preferences;

  Future _isPreferenceInstanceReady;

  // Final static instance of class initialized by private constructor
  static final AppPreferences _instance = AppPreferences._internal();

  // Factory Constructor
  factory AppPreferences() => _instance;

  /// AppPreference Private Internal Constructor -> AppPreference
  /// @param->_
  /// @usage-> Initialize SharedPreference object and notify when operation is complete to future variable.
  AppPreferences._internal() {
    _isPreferenceInstanceReady = SharedPreferences.getInstance()
        .then((preferences) => _preferences = preferences);
  }

  // GETTER for isPreferenceReady future
  Future get isPreferenceReady => _isPreferenceInstanceReady;

  /// Set Logged-In Method -> void
  /// @param -> @required isLoggedIn -> bool
  /// @usage -> Set value of IS_LOGGED_IN in preferences
  void setLoggedIn({@required bool isLoggedIn}) => _setPreference(
      prefName: PREF_IS_LOGGED_IN,
      prefValue: isLoggedIn,
      prefType: PrefType.PREF_TYPE_BOOL);

  /// Get Logged-In Method -> Future<bool>
  /// @param -> _
  /// @usage -> Get value of IS_LOGGED_IN from preferences
  Future<bool> getLoggedIn() async =>
      await _getPreference(prefName: PREF_IS_LOGGED_IN) ??
          false; // Check value for NULL. If NULL provide default value as FALSE.

  void setFirebaseToken({@required String token}) => _setPreference(
      prefName: PREF_FIREBASE_TOKEN,
      prefValue: token,
      prefType: PrefType.PREF_TYPE_STRING);

  Future<String> getFirebaseToken() async {
    var mToken = await _getPreference(prefName: PREF_FIREBASE_TOKEN);
    return mToken;
  }

  void _setPreference(
      {@required String prefName,
      @required dynamic prefValue,
      @required PrefType prefType}) {
    // Make switch for Preference Type i.e. Preference-Value's data-type
    switch (prefType) {
      // prefType is bool
      case PrefType.PREF_TYPE_BOOL:
        {
          _preferences.setBool(prefName, prefValue);
          break;
        }
      // prefType is int
      case PrefType.PREF_TYPE_INTEGER:
        {
          _preferences.setInt(prefName, prefValue);
          break;
        }
      // prefType is double
      case PrefType.PREF_TYPE_DOUBLE:
        {
          _preferences.setDouble(prefName, prefValue);
          break;
        }
      // prefType is String
      case PrefType.PREF_TYPE_STRING:
        {
          _preferences.setString(prefName, prefValue);
          break;
        }

      case PrefType.PREF_TYPE_OBJECT:
        {
          _preferences.setString(prefName, jsonEncode(prefValue));
          break;
        }
    }
  }

  Future<dynamic> _getPreference({@required prefName}) async =>
      _preferences.get(prefName);
}