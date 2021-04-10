const functions = require("firebase-functions");
const admin = require("firebase-admin");

admin.initializeApp(functions.config().firebase);

exports.sendFollowingNotification =
    functions.database
        .ref("/ORDERS/{appId}/{cafeId}/{orderId}")
        .onCreate((snapshot, context) => {
          const appId = context.params.appId;
          const cafeId = context.params.cafeId;
          console.log("appId: ", appId);
          console.log("cafeId: ", cafeId);

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
                      cafe_id: cafeId,
                    },
                    topic: "notification",
                  };

                  return admin.messaging()
                      .send(payload)
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
