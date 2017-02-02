(function() {
    'use strict';

    angular
        .module('jHipsterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('sonmySuffix', {
            parent: 'entity',
            url: '/sonmySuffix',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Sons'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/son/sonsmySuffix.html',
                    controller: 'SonMySuffixController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('sonmySuffix-detail', {
            parent: 'entity',
            url: '/sonmySuffix/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Son'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/son/sonmySuffix-detail.html',
                    controller: 'SonMySuffixDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Son', function($stateParams, Son) {
                    return Son.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'sonmySuffix',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('sonmySuffix-detail.edit', {
            parent: 'sonmySuffix-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/son/sonmySuffix-dialog.html',
                    controller: 'SonMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Son', function(Son) {
                            return Son.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('sonmySuffix.new', {
            parent: 'sonmySuffix',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/son/sonmySuffix-dialog.html',
                    controller: 'SonMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                age: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('sonmySuffix', null, { reload: 'sonmySuffix' });
                }, function() {
                    $state.go('sonmySuffix');
                });
            }]
        })
        .state('sonmySuffix.edit', {
            parent: 'sonmySuffix',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/son/sonmySuffix-dialog.html',
                    controller: 'SonMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Son', function(Son) {
                            return Son.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('sonmySuffix', null, { reload: 'sonmySuffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('sonmySuffix.delete', {
            parent: 'sonmySuffix',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/son/sonmySuffix-delete-dialog.html',
                    controller: 'SonMySuffixDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Son', function(Son) {
                            return Son.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('sonmySuffix', null, { reload: 'sonmySuffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
