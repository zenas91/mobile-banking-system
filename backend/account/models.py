from django.db import models
from django.contrib.auth.models import User
from django.db.models.signals import post_save
from django.dispatch import receiver

class Base(models.Model):
    id = models.AutoField(primary_key=True)
    created = models.DateTimeField(auto_now_add=True)
    modified = models.DateTimeField(auto_now=True, blank=True, null=True)
    enabled = models.BooleanField(default=True)

    class Meta:
        abstract = True

class Address(Base):
    street = models.CharField(max_length=100, blank=False)
    housenumber = models.IntegerField(blank=True)
    postcode = models.IntegerField(blank=True)
    city = models.CharField(max_length=100, blank=False)
    state = models.CharField(max_length=100, blank=False)
    country = models.CharField(max_length=100, blank=False)
    owner = models.ForeignKey(User, on_delete=models.CASCADE)

    def __str__(self):
        return str(self.street)
 
    class Meta:
        pass

class Account(Base):
    iban = models.CharField(unique=True, max_length=20, blank=False)
    number = models.CharField(unique=True, max_length=20, blank=False)
    owner = models.ForeignKey(User, on_delete=models.CASCADE, related_name='account_owner')

    ACCOUNTTYPE = (
        ('SAVINGS', 'Savings'),
        ('CURRENT', 'Current'),
    )
    act_type = models.CharField(max_length=20, choices=ACCOUNTTYPE, default=ACCOUNTTYPE[0])
    balance = models.IntegerField(blank=True)
    overdraft = models.BooleanField(default=False)

    def __str__(self):
        return str(self.iban)

    class Meta:
        pass
    
class Transaction(Base):
    ref = models.CharField(unique=True, max_length=20, blank=False)
    debit = models.ForeignKey(Account, on_delete=models.CASCADE, related_name='debit')
    credit = models.ForeignKey(Account, on_delete=models.CASCADE, related_name='credit')

    TRANSACTIONTYPE = (
        ('TRANSFER', 'Transfer'),
        ('CREDIT', 'Credit'),
        ('DEBIT', 'Debit'),
    )
    trans_type = models.CharField(max_length=20, choices=TRANSACTIONTYPE, default=TRANSACTIONTYPE[0])

    amount = models.IntegerField(blank=False)
    balbeforedebit = models.IntegerField(blank=True)
    balafterdebit = models.IntegerField(blank=True)
    balbeforecredit = models.IntegerField(blank=True)
    balaftercredit = models.IntegerField(blank=True)
    remark = models.CharField(max_length=150, blank=True)

    def __str__(self):
        return str(self.ref)

    class Meta:
        pass