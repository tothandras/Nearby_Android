function getFbId(user) {
  return user.get('authData').facebook.id;
}

Parse.Cloud.afterSave(Parse.User, function(request) {
  Parse.Cloud.useMasterKey();
  var user = request.object;

  user.set('facebookId', getFbId(user));

  user.save();
});

function nofityUser(user) {
  // TODO
}

Parse.Cloud.job('nearbyFriends', function(request, status) {
  Parse.Cloud.useMasterKey();
  // Query for all users
  var query = new Parse.Query(Parse.User);
  query.each(function(user) {
    var location = user.get('location');
    var friends = user.get('friends');

    var userQuery = new Parse.Query(Parse.User);
    friends.forEach(function(friend){
      userQuery.contains('facebookId', friend);
      userQuery.near('location', location);
      userQuery.find({
        success: function(nearbyFriend) {
          console.log(nearbyFriend.get('name'));
        }
      });
    });
  }).then(function() {
    // Set the job's success status
    status.success('Getting nearby friends: completed successfully.');
  }, function(error) {
    // Set the job's error status
    status.error('Getting nearby friends: something went wrong');
  });
});
