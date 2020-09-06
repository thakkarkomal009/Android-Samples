import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter/material.dart';
import 'package:notifier/notifier_provider.dart';
import 'package:send_message_fcm/app/app.dart';
import 'package:send_message_fcm/app/resource/app_styles.dart';

void main() async {
  bool USE_FIRESTORE_EMULATOR = false;
  WidgetsFlutterBinding.ensureInitialized();
  await Firebase.initializeApp();
  if (USE_FIRESTORE_EMULATOR) {
    FirebaseFirestore.instance.settings = Settings(
        host: 'localhost:8080', sslEnabled: false, persistenceEnabled: false);
  }
  runApp(NotifierProvider(
      child: new MaterialApp(
          debugShowCheckedModeBanner: false,
          home: App())));
}

class MyApp extends StatefulWidget {
  @override
  MyAppState createState() => MyAppState();
}

class MyAppState extends State<MyApp> {
  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return null;
  }
}
