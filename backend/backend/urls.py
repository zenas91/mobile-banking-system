"""backend URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/3.1/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  path('', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  path('', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.urls import include, path
    2. Add a URL to urlpatterns:  path('blog/', include('blog.urls'))
"""
from django.contrib import admin
from django.urls import path
from django.conf.urls import url, include
from rest_framework import routers
from account import views

router = routers.DefaultRouter() 
router.register(r'users', views.UserViewSet)
router.register(r'addresses', views.AddressViewSet)
router.register(r'addressSearch', views.AddressSearchViewSet, basename='address_view')
router.register(r'accounts', views.AccountViewSet)
router.register(r'accountSearch', views.AccountSearchViewSet, basename='account_view')
router.register(r'transactions', views.TransactionViewSet)
router.register(r'search', views.SearchViewSet, basename='search_view')

urlpatterns = [
    url(r'^', include(router.urls)),
    url(r'^api-auth/', include('rest_framework.urls', namespace='rest_framework')),
    path('admin/', admin.site.urls),
    path('api-token-auth/', views.CustomAuthToken.as_view(), name='api-token-auth'),
]