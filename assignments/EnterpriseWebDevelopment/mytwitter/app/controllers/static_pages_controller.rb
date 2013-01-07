class StaticPagesController < ApplicationController
  def home
    @microposts = current_user.microposts if signed_in?
    @micropost = current_user.microposts.build if signed_in?
    @movie_review = current_user.movie_reviews.build if signed_in?
    @genres = Genre.all if signed_in?
    @mediatypes = Medium.all if signed_in?
  end

  def help
  end
  
  def about
  end
end
