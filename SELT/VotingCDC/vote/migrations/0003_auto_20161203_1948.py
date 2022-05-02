# -*- coding: utf-8 -*-
# Generated by Django 1.9 on 2016-12-04 01:48
from __future__ import unicode_literals

from django.db import migrations
import multiselectfield.db.fields


class Migration(migrations.Migration):

    dependencies = [
        ('vote', '0002_auto_20161201_1421'),
    ]

    operations = [
        migrations.AlterField(
            model_name='election_info',
            name='candidates_choice',
            field=multiselectfield.db.fields.MultiSelectField(choices=[(b'Sakura Haruna', b'Sakura Haruna'), (b'Sasuke Uchiha', b'Sasuke Uchiha'), (b'Uzumaki Naruto', b'Uzumaki Naruto'), (b'Buttercup Oie', b'Buttercup Oie')], max_length=50),
        ),
        migrations.AlterField(
            model_name='election_info',
            name='precincts_range',
            field=multiselectfield.db.fields.MultiSelectField(choices=[(b'50001 - 50101', b'50001 - 50101'), (b'50102 - 50202', b'50102 - 50202'), (b'50203 - 50303', b'50203 - 50303'), (b'50304 - 50404', b'50304 - 50404'), (b'50405 - 50505', b'50405 - 50505'), (b'50506 - 50606', b'50506 - 50606'), (b'50607 - 50707', b'50607 - 50707'), (b'50708 - 50808', b'50708 - 50808'), (b'50809 - 50909', b'50809 - 50909'), (b'50910 - 51010', b'50910 - 51010'), (b'51011 - 51111', b'51011 - 51111'), (b'51112 - 51212', b'51112 - 51212'), (b'51213 - 51313', b'51213 - 51313'), (b'51314 - 51414', b'51314 - 51414'), (b'51415 - 51515', b'51415 - 51515'), (b'51516 - 51616', b'51516 - 51616'), (b'51617 - 51717', b'51617 - 51717'), (b'51718 - 51818', b'51718 - 51818'), (b'51819 - 51919', b'51819 - 51919'), (b'51920 - 52020', b'51920 - 52020'), (b'52021 - 52121', b'52021 - 52121'), (b'52122 - 52222', b'52122 - 52222'), (b'52223 - 52323', b'52223 - 52323'), (b'52324 - 52424', b'52324 - 52424'), (b'52425 - 52525', b'52425 - 52525'), (b'52526 - 52626', b'52526 - 52626'), (b'52627 - 52727', b'52627 - 52727'), (b'52728 - 52828', b'52728 - 52828')], max_length=391),
        ),
    ]