import 'dart:async';

import 'package:flutter/material.dart';
import 'package:send_message_fcm/app/app.dart';
import 'package:send_message_fcm/app/app_routes.dart';

class Splash extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      home: SplashView(),
      onGenerateRoute: AppRoutes().getRoutes,
    );
  }
}

class SplashView extends StatefulWidget {
  @override
  State<StatefulWidget> createState() => SplashState();
}

class SplashState extends State<SplashView>
    with SingleTickerProviderStateMixin {
  startTimeout() async {
    return Timer(const Duration(seconds: 1), handleTimeout);
  }

  void handleTimeout() async {
    App().getAppPreferences().getLoggedIn().then((isLoggedIn) {
      isLoggedIn
          ? Navigator.pushReplacementNamed(
              context, AppRoutes.APP_ROUTE_USER_LIST)
          : Navigator.pushReplacementNamed(
              context, AppRoutes.APP_ROUTE_CREATE_USER);
    });
  }

  @override
  void initState() {
    super.initState();
    startTimeout();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Theme.of(context).primaryColorDark,
      body: Center(
          child: Image(
        image: AssetImage("images/logo.png"),
        width: 180,
        height: 180,
      )),
    );
  }
}
