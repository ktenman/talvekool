(function() {
    'use strict';

    angular
        .module('talvekoolApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('osaleja', {
            parent: 'entity',
            url: '/osaleja',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'talvekoolApp.osaleja.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/osaleja/osalejas.html',
                    controller: 'OsalejaController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('osaleja');
                    $translatePartialLoader.addPart('sugu');
                    $translatePartialLoader.addPart('oskustase');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('osaleja-detail', {
            parent: 'entity',
            url: '/osaleja/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'talvekoolApp.osaleja.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/osaleja/osaleja-detail.html',
                    controller: 'OsalejaDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('osaleja');
                    $translatePartialLoader.addPart('sugu');
                    $translatePartialLoader.addPart('oskustase');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Osaleja', function($stateParams, Osaleja) {
                    return Osaleja.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'osaleja',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('osaleja-detail.edit', {
            parent: 'osaleja-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/osaleja/osaleja-dialog.html',
                    controller: 'OsalejaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Osaleja', function(Osaleja) {
                            return Osaleja.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('osaleja.new', {
            parent: 'osaleja',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/osaleja/osaleja-dialog.html',
                    controller: 'OsalejaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nimi: null,
                                sugu: null,
                                oskustase: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('osaleja', null, { reload: 'osaleja' });
                }, function() {
                    $state.go('osaleja');
                });
            }]
        })
        .state('osaleja.edit', {
            parent: 'osaleja',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/osaleja/osaleja-dialog.html',
                    controller: 'OsalejaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Osaleja', function(Osaleja) {
                            return Osaleja.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('osaleja', null, { reload: 'osaleja' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('osaleja.delete', {
            parent: 'osaleja',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/osaleja/osaleja-delete-dialog.html',
                    controller: 'OsalejaDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Osaleja', function(Osaleja) {
                            return Osaleja.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('osaleja', null, { reload: 'osaleja' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
