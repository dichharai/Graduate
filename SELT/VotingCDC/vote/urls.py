from django.conf.urls import url
from vote.views import (
	landing_view, login_view, register_view, logout_view, post_candidates, candidates_detail, 
	staff_view, user_view, candidates, candidates_update, candidate_deleted,
	candidate_delete_confirmation,user_info,user_info_update, view_candidates,
	post_election,election_detail, elections, election_update, view_candidates_detail,
	user_elections, user_election_detail, user_vote_success, election_delete_confirmation,
	election_deleted)
	# user_results_view, user_polls_view)

from django.conf import settings 
from django.conf.urls.static import static

from . import views


urlpatterns = [
	url(r'^$', landing_view, name='landing'),
	url(r'^login/$', login_view, name='login'),
	url(r'^logout/$', logout_view, name='logout'),
	url(r'^admin/staff/(?P<username>[a-z]+)/$', staff_view, name="welcome_staff"),
	url(r'user/(?P<u_id>\d+)/$', user_view, name="welcome_user"),
	url(r'^register/$', register_view, name='register'),
	url(r'^admin/post_candidates/$',post_candidates, name='post_candidates'),
	url(r'^candidates_detail/(?P<c_id>\d+)/', candidates_detail, name='candidates_detail'),
	url(r'^admin/candidates_list/$', candidates, name="candidates_list" ),
	url(r'^admin/candidates/(?P<c_id>\d+)/edit/$', candidates_update , name="candidates_update"),
	url(r'^admin/candidates/(?P<c_id>\d+)/delete/$', candidate_delete_confirmation , name="candidate_delete_confirmation"),
	url(r'^admin/candidates/(?P<c_id>\d+)/delete_success/$', candidate_deleted , name="candidate_deleted"),
	url(r'^admin/post_election/$', post_election, name='post_election'),
	url(r'^admin/election_detail/(?P<e_id>\d+)/', election_detail, name='election_detail'),
	url(r'^admin/elections/(?P<e_id>\d+)/edit/$', election_update , name="election_update"),
	url(r'^admin/elections/(?P<e_id>\d+)/delete/$', election_delete_confirmation , name="election_delete_confirmation"),
	url(r'^admin/elections/(?P<e_id>\d+)/delete_success/$', election_deleted , name="election_deleted"),
	url(r'^admin/elections_form_list/$', elections, name="elections_list" ),
	url(r'^user/(?P<u_id>\d+)/profile/$', user_info, name="user_info"),
	url(r'^user/(?P<u_id>\d+)/edit/$', user_info_update, name="user_info_update"),
	url(r'^user/(?P<u_id>\d+)/view_candidates/$', view_candidates, name='view_candidates'),
	url(r'^user/(?P<u_id>\d+)/candidates_detail/(?P<c_id>\d+)/$', view_candidates_detail, name='view_candidates_detail'),
	url(r'^user/(?P<u_id>\d+)/elections/$', user_elections, name="user_elections_list"),
	url(r'^user/(?P<u_id>\d+)/election_detail/(?P<e_id>\d+)/', user_election_detail, name='user_election_detail'),
	url(r'^user/(?P<u_id>\d+)/election_detail/(?P<e_id>\d+)/success', user_vote_success, name='user_vote_success'),
	# url(r'^user/(?P<u_id>\d+)/results/$', user_results_view, name="user_results"),

	#for voting
	url(r'^(?P<question_id>\d+)/$', views.detail, name='detail'),
	url(r'^(?P<question_id>\d+)/results/$', views.results, name='results'),
	url(r'^(?P<question_id>\d+)/vote/$', views.vote, name='vote'),
	url(r'^user/(?P<u_id>\d+)/poll_list/$', views.poll_list, name="poll_list"),
]

if settings.DEBUG:
	urlpatterns += static(settings.STATIC_URL, document_root = settings.STATIC_ROOT)
	urlpatterns += static(settings.MEDIA_URL, document_root = settings.MEDIA_ROOT)