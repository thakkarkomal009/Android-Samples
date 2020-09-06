import 'dart:async';

import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:flutter/material.dart';
import 'package:send_message_fcm/app/app.dart';
import 'package:send_message_fcm/app/app_routes.dart';
import 'package:send_message_fcm/app/local_database/app_preferences.dart';
import 'package:send_message_fcm/app/resource/app_colors.dart';
import 'package:send_message_fcm/app/resource/app_string.dart';
import 'package:send_message_fcm/app/resource/app_styles.dart';

class CreateUser extends StatefulWidget {
  @override
  _CreateUserState createState() => _CreateUserState();
}

class _CreateUserState extends State<CreateUser> {
  final _formKey = GlobalKey<FormState>();

  TextEditingController userNameController = new TextEditingController();
  TextEditingController passwordController = new TextEditingController();
  final FocusNode userNameFocus = FocusNode();
  final FocusNode passwordFocus = FocusNode();
  final FocusNode _loginBtnFocus = FocusNode();
  var _passwordVisible = false;
  String userName;
  String password;
  AppPreferences _appPreferences = AppPreferences();

  @override
  Widget build(BuildContext context) {
    return getCreateUser(context);
  }

  @override
  void initState() {
    super.initState();
  }

  Widget getCreateUser(context) {
    return Scaffold(
        resizeToAvoidBottomInset: false,
        //it will prevent layout sink while keyboard open issue.
        body: Container(
            color: Colors.white,
            child: Container(
              margin: EdgeInsets.only(top: 60),
              child: Form(
                key: _formKey,
                child: Container(
                    //color : Colors.white,
                    decoration: BoxDecoration(color: Colors.white),
                    child: Padding(
                        padding: EdgeInsets.all(15),
                        child: new Column(children: <Widget>[
                          Padding(
                            padding: EdgeInsets.all(30),
                            child: Align(
                                alignment: Alignment.center,
                                child: Image(
                                  image: AssetImage("images/logo.png"),
                                  width: 100,
                                  height: 100,
                                )),
                          ),
                          Padding(
                              padding:
                                  EdgeInsets.only(top: 20, left: 10, right: 10),
                              child: userNameTextField()),
                          Padding(
                              padding: EdgeInsets.only(
                                  top: 15.0, bottom: 15.0, left: 10, right: 10),
                              child: passTextField()),
                          SizedBox(
                              width: double.infinity,
                              child: Container(
                                  margin: EdgeInsets.only(
                                      left: 10, right: 10, top: 20),
                                  child: FlatButton(
                                    focusNode: _loginBtnFocus,
                                    shape: RoundedRectangleBorder(
                                        borderRadius:
                                            BorderRadius.circular(8.0),
                                        side: BorderSide(
                                            color: AppColors.BUTTON_COLOR)),
                                    color: AppColors.BUTTON_COLOR,
                                    textColor: Colors.white,
                                    padding: EdgeInsets.all(8.0),
                                    onPressed: () async {
                                      if (!_formKey.currentState.validate()) {
                                        return;
                                      }
                                      Timer(const Duration(milliseconds: 500),
                                          () async => {await _submitCommand()});
                                    },
                                    child: Text(
                                        AppStrings.CREATE_USER.toUpperCase(),
                                        style: AppStyles.normalWHiteFontStyle),
                                  ))),
                        ]))),
              ),
            )));
  }

  Widget userNameTextField() {
    return TextFormField(
        focusNode: userNameFocus,
        cursorColor: Colors.black26,
        controller: userNameController,
        decoration: InputDecoration(
            enabledBorder: UnderlineInputBorder(
              borderSide: BorderSide(color: Colors.black26),
            ),
            focusedBorder: UnderlineInputBorder(
              borderSide: BorderSide(color: Colors.black),
            ),
            labelText: AppStrings.USER_NAME,
            labelStyle: TextStyle(
                color: userNameFocus.hasFocus ? Colors.black : Colors.black38),
            suffixIcon: IconButton(
                icon: Icon(
              Icons.person,
              color: Colors.black38,
            ))),
        validator: (val) =>
            val.length < 4 ? AppStrings.ENTER_VALID_USER_NAME : null,
        onSaved: (val) => userName = val);
  }

  Widget passTextField() {
    return TextFormField(
      focusNode: passwordFocus,
      cursorColor: Colors.black26,
      controller: passwordController,
      obscureText: !_passwordVisible,
      //This will obscure text dynamically
      decoration: InputDecoration(
          enabledBorder: UnderlineInputBorder(
            borderSide: BorderSide(color: Colors.black26),
          ),
          focusedBorder: UnderlineInputBorder(
            borderSide: BorderSide(color: Colors.black),
          ),
          labelText: AppStrings.PASSWORD,
          labelStyle: TextStyle(
              color: passwordFocus.hasFocus ? Colors.black : Colors.black38),
          suffixIcon: IconButton(
              onPressed: () {
                setState(() {
                  _passwordVisible = !_passwordVisible;
                });
              },
              icon: Icon(
                // Based on passwordVisible state choose the icon
                _passwordVisible ? Icons.visibility : Icons.visibility_off,
                color: Colors.black38,
              ))),
      validator: (val) =>
          val.length < 4 ? AppStrings.PASSWORD_IS_TOO_SHORT : null,
      onSaved: (val) => password = val,
    );
  }

  void _submitCommand() async {
    if (!_formKey.currentState.validate()) {
      return;
    }
    var firebaseToken = await App().getAppPreferences().getFirebaseToken();
    DocumentReference ref =
        await FirebaseFirestore.instance.collection("users").doc();
    ref.set({
      'id': ref.id,
      'userName': userNameController.text,
      'password': passwordController.text,
      'firebaseToken': firebaseToken
    });

    print("Token:::" + firebaseToken);
    _appPreferences.setLoggedIn(isLoggedIn: true);
    Navigator.pushReplacementNamed(context, AppRoutes.APP_ROUTE_USER_LIST);
  }
}
