function getFbId(user) {
  return user.get('authData').facebook.id;
}

Parse.Cloud.afterSave(Parse.User, function(request) {
  Parse.Cloud.useMasterKey();
  var user = request.object;
  user.set('facebookId', getFbId(user));
  user.save();
});

function nofityUser(user, nearby) {
  var query = new Parse.Query(Parse.Installation);
  query.equalTo('user', user);
  Parse.Push.send({
    where: query,
    data: {
      alert: nearby.get('facebookId'), // TODO replace with name
      title: 'Someone is nearby',
      friendLocation: nearby.get('location')
    }
  }, {
    success: function() {
      // Push was successful
    },
    error: function(error) {
      // Handle error
    }
  });
}

Parse.Cloud.job('nearbyFriends', function(request, status) {
  Parse.Cloud.useMasterKey();
  // Query for all users
  var query = new Parse.Query(Parse.User);
  query.each(function(user) {
    var location = user.get('location');
    var friends = user.get('friends');

    var userQuery = new Parse.Query(Parse.User);
    userQuery.containedIn('facebookId', friends);
    userQuery.near('location', location);
    userQuery.find({
      success: function(results) {
        results.forEach(function(result) {
          nofityUser(user, result);
        });
      }
    });
    return user.save();
  }).then(function() {
    // Set the job's success status
    status.success('Getting nearby friends: completed successfully.');
  }, function(error) {
    // Set the job's error status
    status.error('Getting nearby friends: something went wrong');
  });
});
