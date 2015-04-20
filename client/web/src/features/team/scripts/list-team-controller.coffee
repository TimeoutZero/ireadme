'use strict'

# =============================================
# Module
# =============================================
angular.module 'ProjectTrailApp.controllers'

  # =============================================
  # ToolController
  # =============================================
  .controller 'ListTeamController', [ '$rootScope', '$scope', '$state', '$mdBottomSheet', '$mdDialog', '$mdToast', 'TeamService',
    ($rootScope, $scope, $state, $mdBottomSheet, $mdDialog, $mdToast, TeamService) ->

      # =============================================
      # Attributes
      # =============================================
      $scope.teams       = []
      $scope.currentTeam = null

      # =============================================
      # Methods
      # =============================================
      $scope.onClickItem = (item) ->
        $state.go 'tool.list', teamId : item.id

      $scope.openFormDialog = (item) ->
        $scope.currentTeam = item
        $scope.$broadcast 'openFormDialog', $scope.currentTeam

      $scope.showDeleteDialog = (item) ->
        confirm = $mdDialog.confirm()
          .title('Confirm')
          .content('Are you sure about delete this item?')
          .ariaLabel('Teams')
          .ok('Yes, delete!')
          .cancel('no')

        $mdDialog.show(confirm).then( -> $scope.deleteItem(item) )

      $scope.deleteItem = (item) ->
        promise = TeamService.delete(item)
        promise.success ->
          $mdToast.show( $mdToast.simple().content('Deleted') )
          $scope.getTeams()

      $scope.getTeams = ->
        promise = TeamService.list()
        promise.success (data) -> $scope.teams = data
        promise.error (errorData) ->
        return promise

      $scope.initialize = ->
        $scope.getTeams()


      # =============================================
      # Aux Methods
      # =============================================


      # =============================================
      # Events
      # =============================================
      $scope.$on 'updateTeamList', $scope.getTeams


      # =============================================
      # Initialize
      # =============================================
      $scope.initialize()

      return @

  ]