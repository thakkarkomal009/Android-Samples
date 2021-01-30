import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:toast/toast.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        // This is the theme of your application.
        //
        // Try running your application with "flutter run". You'll see the
        // application has a blue toolbar. Then, without quitting the app, try
        // changing the primarySwatch below to Colors.green and then invoke
        // "hot reload" (press "r" in the console where you ran "flutter run",
        // or simply save your changes to "hot reload" in a Flutter IDE).
        // Notice that the counter didn't reset back to zero; the application
        // is not restarted.
        primarySwatch: Colors.blue,
        // This makes the visual density adapt to the platform that you run
        // the app on. For desktop platforms, the controls will be smaller and
        // closer together (more dense) than on mobile platforms.
        visualDensity: VisualDensity.adaptivePlatformDensity,
      ),
      home: Registration(title: 'Registration'),
    );
  }
}

class Registration extends StatefulWidget {
  Registration({Key key, this.title}) : super(key: key);

  // This widget is the home page of your application. It is stateful, meaning
  // that it has a State object (defined below) that contains fields that affect
  // how it looks.

  // This class is the configuration for the state. It holds the values (in this
  // case the title) provided by the parent (in this case the App widget) and
  // used by the build method of the State. Fields in a Widget subclass are
  // always marked "final".

  final String title;

  @override
  _RegistrationState createState() => _RegistrationState();
}

class _RegistrationState extends State<Registration> {
  GlobalKey<FormState> formKey = GlobalKey<FormState>();
  TextEditingController _nameController = TextEditingController();
  TextEditingController _emailController = TextEditingController();
  TextEditingController _passwordController = TextEditingController();
  TextEditingController _confirmPasswordController = TextEditingController();

  FocusNode _nameFocus = FocusNode();
  FocusNode _emailFocus = FocusNode();
  FocusNode _passwordFocus = FocusNode();
  FocusNode _confirmPasswordFocus = FocusNode();

  bool _hasNameInputError = false;
  bool _hasEmailInputError = false;
  bool _hasPasswordInputError = false;
  bool _hasConfirmPasswordInputError = false;

  var nameValidation = "";
  var emailValidation = "";
  var passwordValidation = "";
  var confirmPasswordValidation = "";

  bool autoValidate = false;

  @override
  Widget build(BuildContext context) {
    // This method is rerun every time setState is called, for instance as done
    // by the _incrementCounter method above.
    //
    // The Flutter framework has been optimized to make rerunning build methods
    // fast, so that you can just rebuild anything that needs updating rather
    // than having to individually change instances of widgets.
    return Scaffold(
      appBar: AppBar(
        // Here we take the value from the MyHomePage object that was created by
        // the App.build method, and use it to set our appbar title.
        title: Text(widget.title),
      ),
      body: Stack(
        children: [
          Container(
            margin: EdgeInsets.only(top: 20),
            child: Form(
              key: formKey,
              autovalidate: autoValidate,
              child: SingleChildScrollView(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.stretch,
                  mainAxisAlignment: MainAxisAlignment.start,
                  children: [
                    Padding(
                      padding: EdgeInsets.symmetric(
                          horizontal: setWidth(20, context)),
                      child: Column(
                        children: [
                          TextFormField(
                            controller: _nameController,
                            textInputAction: TextInputAction.next,
                            keyboardType: TextInputType.text,
                            style: TextStyle(fontSize: 14),
                            decoration: InputDecoration(
                                labelText: "Name",
                                errorText:
                                    _hasNameInputError ? nameValidation : null,
                                focusedBorder: OutlineInputBorder(
                                    borderSide:
                                        BorderSide(color: Colors.black54)),
                                enabledBorder: new UnderlineInputBorder(
                                    borderSide:
                                        new BorderSide(color: Colors.black54))),
                            validator: (value) => validateName(value),
                            onChanged: (value) {
                              setState(() {
                                _hasNameInputError = isNameValid(value);
                                nameValidation = validateName(value);
                              });
                            },
                            focusNode: _nameFocus,
                            onFieldSubmitted: (value) {
                              _nameFocus.unfocus();
                              FocusScope.of(context).requestFocus(_emailFocus);
                            },
                          ),
                          SizedBox(
                            height: 10.0,
                          ),
                          TextFormField(
                            controller: _emailController,
                            textInputAction: TextInputAction.next,
                            keyboardType: TextInputType.emailAddress,
                            style: TextStyle(fontSize: 14),
                            decoration: InputDecoration(
                                labelText: "Email",
                                errorText: _hasEmailInputError
                                    ? emailValidation
                                    : null,
                                focusedBorder: OutlineInputBorder(
                                    borderSide:
                                        BorderSide(color: Colors.black54)),
                                enabledBorder: new UnderlineInputBorder(
                                    borderSide:
                                        new BorderSide(color: Colors.black54))),
                            validator: (value) => validateEmail(value),
                            onChanged: (value) {
                              setState(() {
                                _hasEmailInputError = isEmailValid(value);
                                emailValidation = validateEmail(value);
                              });
                            },
                            focusNode: _emailFocus,
                            onFieldSubmitted: (value) {
                              _emailFocus.unfocus();
                              FocusScope.of(context)
                                  .requestFocus(_passwordFocus);
                            },
                          ),
                          SizedBox(
                            height: 10.0,
                          ),
                          TextFormField(
                            controller: _passwordController,
                            textInputAction: TextInputAction.next,
                            keyboardType: TextInputType.text,
                            style: TextStyle(fontSize: 14),
                            decoration: InputDecoration(
                                labelText: "Password",
                                errorText: _hasPasswordInputError
                                    ? passwordValidation
                                    : null,
                                focusedBorder: OutlineInputBorder(
                                    borderSide:
                                        BorderSide(color: Colors.black54)),
                                enabledBorder: new UnderlineInputBorder(
                                    borderSide:
                                        new BorderSide(color: Colors.black54))),
                            validator: (value) => validatePassword(value),
                            onChanged: (value) {
                              setState(() {
                                _hasPasswordInputError = isPasswordValid(value);
                                passwordValidation = validatePassword(value);
                              });
                            },
                            obscureText: true,
                            focusNode: _passwordFocus,
                            onFieldSubmitted: (value) {
                              _passwordFocus.unfocus();
                              FocusScope.of(context)
                                  .requestFocus(_confirmPasswordFocus);
                            },
                          ),
                          SizedBox(
                            height: 10.0,
                          ),
                          TextFormField(
                            controller: _confirmPasswordController,
                            textInputAction: TextInputAction.done,
                            keyboardType: TextInputType.text,
                            style: TextStyle(fontSize: 14),
                            decoration: InputDecoration(
                                labelText: "Confirm Password",
                                errorText: _hasConfirmPasswordInputError
                                    ? confirmPasswordValidation
                                    : null,
                                focusedBorder: OutlineInputBorder(
                                    borderSide:
                                        BorderSide(color: Colors.black54)),
                                enabledBorder: new UnderlineInputBorder(
                                    borderSide:
                                        new BorderSide(color: Colors.black54))),
                            validator: (value) => validateConfirmPassword(
                                value, _passwordController.text),
                            onChanged: (value) {
                              setState(() {
                                _hasConfirmPasswordInputError =
                                    isConfirmPasswordValid(
                                        value, _passwordController.text);
                                confirmPasswordValidation =
                                    validateConfirmPassword(
                                        value, _passwordController.text);
                              });
                            },
                            obscureText: true,
                            focusNode: _confirmPasswordFocus,
                          ),
                          SizedBox(
                            height: 20.0,
                          ),
                          Padding(
                            padding: EdgeInsets.symmetric(
                                horizontal: setWidth(16.0, context)),
                            child: SizedBox(
                              width: double.infinity,
                              child: RaisedButton(
                                  elevation: 0.0,
                                  color: Colors.blueGrey,
                                  shape: RoundedRectangleBorder(
                                      borderRadius:
                                          new BorderRadius.circular(40.0),
                                      side: BorderSide(color: Colors.blueGrey)),
                                  child: Padding(
                                    padding: EdgeInsets.symmetric(
                                        horizontal: setWidth(12.0, context)),
                                    child: Row(
                                      mainAxisAlignment:
                                          MainAxisAlignment.center,
                                      crossAxisAlignment:
                                          CrossAxisAlignment.center,
                                      children: <Widget>[
                                        Text(
                                          "Submit",
                                        ),
                                      ],
                                    ),
                                  ),
                                  onPressed: () => _handleLoginButtonClick()),
                            ),
                          ),
                        ],
                      ),
                    )
                  ],
                ),
              ),
            ),
          )
        ],
      ),
    );
  }

  double setWidth(double width, BuildContext context) {
    double defaultWidth = 400;
    var screenWidth = MediaQuery.of(context).size.width;
    return width * (screenWidth / defaultWidth);
  }

  String validateName(String value) {
    var minimumLength = value.length > 3;
    if (value.isEmpty) {
      return "Please enter name";
    } else if (!minimumLength) {
      return "Please enter valid name";
    }
  }

  bool isNameValid(String value) {
    var minimumLength = value.length > 3;
    if (value.isEmpty) {
      return true;
    } else if (!minimumLength) {
      return true;
    } else {
      return false;
    }
  }

  String validateEmail(String value) {
    if (value.isEmpty) {
      return "Please enter email";
    }
    Pattern pattern =
        r'^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$';
    RegExp regex = new RegExp(pattern);
    if (!regex.hasMatch(value)) {
      return 'Please enter valid email';
    }
    return null;
  }

  bool isEmailValid(String value) {
    if (value.isEmpty) {
      return true;
    }
    Pattern pattern =
        r'^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$';
    RegExp regex = new RegExp(pattern);
    if (!regex.hasMatch(value)) {
      return true;
    }
    return false;
  }

  bool isPasswordValid(String password) {
    if (password.isEmpty) {
      return true;
    } else if (!checkPasswordValidation(password)) {
      return true;
    }
    return false;
  }

  String validatePassword(String value) {
    if (value.isEmpty) {
      return "Please enter password";
    } else if (!checkPasswordValidation(value)) {
      return "Please enter valid password";
    }
    return null;
  }

  static bool isDigit(String s, int idx) =>
      "0".compareTo(s[idx]) <= 0 && "9".compareTo(s[idx]) >= 0;

  bool checkPasswordValidation(String value) {
    bool isValid = false;
    bool hasUppercase = false;
    bool minLength = false;
    bool hasDigits = false;
    bool hasSpecialCharacters = false;
    var character = '';
    var i = 0;
    RegExp regExp = new RegExp(r'[!@#$%^&*(),.?":{}|<>]');
    if (!value?.isEmpty) {
      minLength = value.length >= 8;

      // Check if valid special characters are present
      hasSpecialCharacters = value.contains(regExp);
      while (i < value.length) {
        character = value.substring(i, i + 1);
        //print(character);

        if (isDigit(character, 0)) {
          hasDigits = true;
        } else {
          if (!character.contains(regExp) &&
              character == character.toUpperCase()) {
            hasUppercase = true;
          }
        }
        i++;
      }
    }
    isValid = hasDigits & hasUppercase & minLength & hasSpecialCharacters;
    return isValid;
  }

  String validateConfirmPassword(String confirmPassword, String password) {
    if (confirmPassword.isEmpty) {
      return "Please enter confirm password";
    }
    if (confirmPassword != password) {
      return "Password and confirm password should be same";
    }
    return null;
  }

  bool isConfirmPasswordValid(String confirmPassword, String password) {
    if (confirmPassword.isEmpty) {
      return true;
    }
    if (confirmPassword != password) {
      return true;
    }
    return false;
  }

  _handleLoginButtonClick() {
    if (formKey.currentState.validate()) {
      print("validate");
      Toast.show("Your account registered successfully", context, duration: Toast.LENGTH_LONG, gravity:  Toast.BOTTOM);
    } else {
      setState(() {
        autoValidate = true;
      });
    }
  }
}
