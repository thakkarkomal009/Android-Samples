import 'package:firebase_core/firebase_core.dart';
import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter/material.dart';
import 'package:notifier/main_notifier.dart';
import 'package:notifier/notifier_provider.dart';
import 'package:send_message_fcm/app/app_routes.dart';
import 'package:send_message_fcm/app/local_database/app_preferences.dart';

class App extends StatefulWidget {
  static final App instance = App.internal();
  App.internal(); //: super(key: key);//{Key key, @required this.notifier}

  factory App() => instance;

  @override
  Widget build(BuildContext context) {
    print("build of app");
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      onGenerateRoute: getAppRoutes().getRoutes,
    );
  }

  AppPreferences getAppPreferences() {
    return AppPreferences();
  }

  AppRoutes getAppRoutes() {
    return AppRoutes();
  }

  @override
  State<StatefulWidget> createState() {
    // TODO: implement createState
    return MyAppState();
  }
}

class MyAppState extends State<App> {
  String _message = '';
  String firebaseToken = "";
  AppPreferences appPreferences = AppPreferences();
  Notifier _notifier;

  final FirebaseMessaging _firebaseMessaging = FirebaseMessaging();

  @override
  void initState() {
    // TODO: implement initState
    super.initState();

    _firebaseMessaging.getToken().then((token) => _setToken(token));
    getMessage();
  }

  void _setToken(token) {
    print("FCM Token::" + token);
    appPreferences.setFirebaseToken(token: token);
    firebaseToken = token;
  }

  void getMessage() {
    _firebaseMessaging.configure(
        onMessage: (Map<String, dynamic> message) async {
      print('on message $message');
      _notifier.notify('action', message);
      setState(() => _message = message["notification"]["title"]);
    }, onResume: (Map<String, dynamic> message) async {
      print('on resume $message');
      setState(() => _message = message["notification"]["title"]);
    }, onLaunch: (Map<String, dynamic> message) async {
      print('on launch $message');
      setState(() => _message = message["notification"]["title"]);
    });
  }

  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    _notifier = NotifierProvider.of(context);
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      onGenerateRoute: getAppRoutes().getRoutes,

    );
  }

  AppRoutes getAppRoutes() {
    return AppRoutes();
  }
}
