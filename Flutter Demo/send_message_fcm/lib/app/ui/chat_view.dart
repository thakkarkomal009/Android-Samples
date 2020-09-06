import 'dart:async';
import 'dart:io';
import 'package:flutter/material.dart';
import 'package:http/http.dart';
import 'package:notifier/main_notifier.dart';
import 'package:send_message_fcm/app/app_routes.dart';
import 'package:send_message_fcm/app/model/user_model.dart';
import 'package:send_message_fcm/app/resource/app_colors.dart';
import 'package:send_message_fcm/app/resource/app_string.dart';

class ChatView extends StatelessWidget {
  final UserModel data;
  final Notifier notifier;

  const ChatView({
    Key key,
    @required this.notifier,
    @required this.data,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      home: ChatListView(notifier: this.notifier, data: this.data),
      onGenerateRoute: AppRoutes().getRoutes,
    );
  }
}

class ChatListView extends StatefulWidget {
  final UserModel data;
  final Notifier notifier;

  const ChatListView({
    Key key,
    @required this.notifier,
    @required this.data,
  }) : super(key: key);

  @override
  State<StatefulWidget> createState() => ChatListState();
}

class ChatListState extends State<ChatListView> {
  bool isLoading = true;
  final messageController = TextEditingController();
  final FocusNode titleTextFocus = FocusNode();
  final FocusNode bodyTextFocus = FocusNode();
  TextEditingController titleController = new TextEditingController();
  TextEditingController bodyController = new TextEditingController();
  String titleTextValue = "";
  String bodyTextValue = "";

  @override
  void initState() {
    super.initState();
    print("Initstate call userlist ");
    setState(() {
      this.isLoading = false;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
          title: Align(
              alignment: Alignment.topLeft,
              child: FlatButton(
                  child: Text(
                widget.data.userName,
                style: TextStyle(color: Colors.white, fontSize: 20),
              )))),
      body: body(context),
    );
  }

  Widget body(context) {
    return Container(
      margin: EdgeInsets.only(top: 10),
      padding: EdgeInsets.fromLTRB(10, 0, 10, 10),
      color: Theme.of(context).canvasColor,
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: <Widget>[
          Padding(
              padding: EdgeInsets.only(top: 20, left: 10, right: 10),
              child: titleText()),
          Padding(
              padding:
                  EdgeInsets.only(top: 15.0, bottom: 15.0, left: 10, right: 10),
              child: bodyText()),
          SizedBox(
              width: double.infinity,
              child: Container(
                  margin: EdgeInsets.only(left: 10, right: 10, top: 20),
                  child: FlatButton(
                    shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(8.0),
                        side: BorderSide(color: AppColors.BUTTON_COLOR)),
                    color: AppColors.BUTTON_COLOR,
                    textColor: Colors.white,
                    padding: EdgeInsets.all(8.0),
                    onPressed: () async {
                      Timer(const Duration(milliseconds: 500),
                          () async => {await postJsonData(this.widget.data)});
                    },
                    child: Text(AppStrings.SEND_MESSAGE_TEXT,
                        style: TextStyle(color: Colors.white)),
                  ))),
          Notifier.of(context).register<Map<String, dynamic>>('action', (data) {
            return Padding(
                padding: EdgeInsets.all(10),
                child: data.data != null
                    ? Text(
                        "Your message: " +
                            data.data["notification"]["body"] +
                            " " +
                            data.data["notification"]["title"],
                        style: TextStyle(fontSize: 26),
                      )
                    : Text(""));
          }),
        ],
      ),
    );
  }

  postJsonData(UserModel data) async {
    // set up POST request arguments
    String url = 'https://fcm.googleapis.com/fcm/send';
    Map<String, String> headers = {
      HttpHeaders.contentTypeHeader: "application/json", // or whatever
      HttpHeaders.authorizationHeader:
          "key=<Your_Project's_Server_Key>",
    };
    String json =
        '{"notification": {"body": "${titleController.text}","title": "${bodyController.text}"},"data": {},"to": "${data.firebaseToken}"}';
    // make POST request
    Response response = await post(url, headers: headers, body: json);
    // check the status code for the result
    int statusCode = response.statusCode;
    // this API passes back the id of the new item added to the body
    String body = response.body;
    print(body);
    print(statusCode);
  }

  Widget titleText() {
    return TextFormField(
        focusNode: titleTextFocus,
        cursorColor: Colors.black26,
        controller: titleController,
        decoration: InputDecoration(
          enabledBorder: UnderlineInputBorder(
            borderSide: BorderSide(color: Colors.black26),
          ),
          focusedBorder: UnderlineInputBorder(
            borderSide: BorderSide(color: Colors.black),
          ),
          labelText: AppStrings.TITLE,
          labelStyle: TextStyle(
              color: titleTextFocus.hasFocus ? Colors.black : Colors.black38),
        ),
        validator: (val) =>
            val.length < 4 ? AppStrings.ENTER_VALID_USER_NAME : null,
        onSaved: (val) => titleTextValue = val);
  }

  Widget bodyText() {
    return TextFormField(
      focusNode: bodyTextFocus,
      cursorColor: Colors.black26,
      controller: bodyController,
      //This will obscure text dynamically
      decoration: InputDecoration(
        enabledBorder: UnderlineInputBorder(
          borderSide: BorderSide(color: Colors.black26),
        ),
        focusedBorder: UnderlineInputBorder(
          borderSide: BorderSide(color: Colors.black),
        ),
        labelText: AppStrings.BODY,
        labelStyle: TextStyle(
            color: bodyTextFocus.hasFocus ? Colors.black : Colors.black38),
      ),
      validator: (val) =>
          val.length < 4 ? AppStrings.PASSWORD_IS_TOO_SHORT : null,
      onSaved: (val) => bodyTextValue = val,
    );
  }
}