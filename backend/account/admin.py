from django.contrib import admin
from account.models import *
# Register your models here.

class AddressAdmin(admin.ModelAdmin):
    pass

class AccountAdmin(admin.ModelAdmin):
    pass

class TransactionAdmin(admin.ModelAdmin):
    pass

admin.site.register(Address, AddressAdmin)

admin.site.register(Account, AccountAdmin)

admin.site.register(Transaction, TransactionAdmin)