import 'package:flutter/material.dart';
import 'package:send_message_fcm/app/ui/chat_view.dart';
import 'package:send_message_fcm/app/ui/create_user.dart';
import 'package:send_message_fcm/app/ui/splash.dart';
import 'package:send_message_fcm/app/ui/user_list.dart';

class AppRoutes {
  static const String APP_ROUTE_SPLASH = "/splash";
  static const String APP_ROUTE_CREATE_USER = "/create_user";
  static const String APP_ROUTE_USER_LIST = "/user_list";
  static const String APP_ROUTE_CHAT_VIEW = "/chat_view";

  Route getRoutes(RouteSettings routeSettings) {
    switch (routeSettings.name) {
      case APP_ROUTE_SPLASH:
        {
          return MaterialPageRoute<void>(
            settings: routeSettings,
            builder: (BuildContext context) => Splash(),
            fullscreenDialog: true,
          );
        }

      case APP_ROUTE_CREATE_USER:
        {
          return MaterialPageRoute<void>(
            settings: routeSettings,
            builder: (BuildContext context) => CreateUser(),
          );
        }
      case APP_ROUTE_USER_LIST:
        {
          return MaterialPageRoute<void>(
            settings: routeSettings,
            builder: (BuildContext context) => UserList(),
          );
        }
      case APP_ROUTE_CHAT_VIEW:
        {
          return MaterialPageRoute<void>(
            settings: routeSettings,
            builder: (BuildContext context) => ChatView(),
          );
        }
      default:
        {
          return MaterialPageRoute<void>(
            settings: routeSettings,
            builder: (BuildContext context) => Splash(),
            fullscreenDialog: true,
          );
        }
    }
  }
}
