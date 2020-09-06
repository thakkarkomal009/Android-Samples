import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:flutter/material.dart';
import 'package:flutter_spinkit/flutter_spinkit.dart';
import 'package:notifier/main_notifier.dart';
import 'package:notifier/notifier_provider.dart';
import 'package:send_message_fcm/app/app_routes.dart';
import 'package:send_message_fcm/app/model/user_model.dart';
import 'package:send_message_fcm/app/resource/app_string.dart';
import 'package:send_message_fcm/app/resource/app_styles.dart';
import 'package:send_message_fcm/app/ui/chat_view.dart';

class UserList extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      home: UserListView(),
      onGenerateRoute: AppRoutes().getRoutes,
    );
  }
}

class UserListView extends StatefulWidget {
  @override
  State<StatefulWidget> createState() => UserListState();
}

class UserListState extends State<UserListView> {
  List<UserModel> userList = new List<UserModel>();
  bool isLoading = true;

  @override
  void initState() {
    super.initState();
    print("Initstate call userlist ");
    setState(() {
      this.isLoading = true;
    });
    getUserListing();
  }

  @override
  Widget build(BuildContext context) {
//    getUserList();
    Notifier _notifier = NotifierProvider.of(context);
    return isLoading
        ? showProgressbar()
        : Scaffold(
            appBar: AppBar(
                title: Align(
                    alignment: Alignment.topLeft,
                    child: FlatButton(
                        child: Text(
                      AppStrings.USER_LIST,
                      style: TextStyle(color: Colors.white, fontSize: 20),
                    )))),
            body: showUserListData(),
          );
  }

  Widget showUserList(BuildContext context) {
    FirebaseFirestore.instance
        .collection('users')
        .get()
        .then((QuerySnapshot querySnapshot) => {
              querySnapshot.docs.forEach((doc) {
                var data = doc.data();
                print(data["userName"]);
              })
            });
  }

  void getUserListing() async {
    userList = List<UserModel>();
    FirebaseFirestore.instance
        .collection('users')
        .get()
        .then((QuerySnapshot querySnapshot) => {FillUserList(querySnapshot)});
  }

  void FillUserList(QuerySnapshot querySnapshot) {
    querySnapshot.docs.forEach((doc) {
      var data = doc.data();
      print(data["userName"]);
      UserModel userModel = UserModel();
      userModel.userName = data["userName"];
      userModel.password = data["password"];
      userModel.firebaseToken = data["firebaseToken"];
      userModel.id = data["id"];
      userList.add(userModel);
    });
    setState(() {
      isLoading = false;
    });
    print("Size::" + userList.length.toString());
  }

  Widget showProgressbar() {
    return const SpinKitRotatingCircle(color: Colors.black54);
  }

  Widget showUserListData() {
    return Container(
      child: Column(children: <Widget>[
        Expanded(
            child: ListView.builder(
                itemCount: userList.length,
                itemBuilder: (BuildContext context, int index) {
                  return new ListTile(
                      title: new Card(
                          elevation: 5.0,
                          child: new Column(children: <Widget>[
                            Align(
                                alignment: Alignment.topLeft,
                                child: Padding(
                                  padding: EdgeInsets.all(5),
                                  child: Padding(
                                    padding: EdgeInsets.all(10),
                                    child: GestureDetector(
                                      onTap: () {
                                        Navigator.push(
                                          context,
                                          MaterialPageRoute(
                                            builder: (context) => ChatView(
                                                data: userList[index],
                                                notifier: Notifier.of(context)),
                                          ),
                                        );
//                                        Navigator.pushNamed(context, AppRoutes.APP_ROUTE_CHAT_VIEW);
                                      },
                                      child: Text(userList[index].userName,
                                          style: AppStyles.titleWhiteStyle),
                                    ),
                                  ),
                                )),
                          ])));
                }))
      ]),
    );
  }
}
