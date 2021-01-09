const functions = require("firebase-functions");
const admin = require("firebase-admin");

admin.initializeApp(functions.config().firebase);

exports.sendFollowingNotification =
    functions.database
        .ref("/ORDERS/{appId}/{orderId}")
        .onCreate((snapshot, context) => {
          const appId = context.params.appId;
          console.log("appId: ", appId);

          const orderId = context.params.orderId;
          console.log("orderId: ", orderId);

          return admin.database()
              .ref("/COMPANY/" + appId + "/token")
              .once("value")
              .then((snap) => {
                const token = snap.val();
                console.log("token: ", token);

                if (token !== "-") {
                  const payload = {
                    data: {
                      app_id: appId,
                    },
                  };

                  return admin.messaging()
                      .sendToDevice(token, payload)
                      .then(function(response) {
                        console.log("Successfully sent message:", response);
                        return;
                      }).catch(function(error) {
                        console.log("Error sending message:", error);
                      });
                } else {
                  return;
                }
              });
        });

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//   functions.logger.info("Hello logs!", {structuredData: true});
//   response.send("Hello from Firebase!");
// });
