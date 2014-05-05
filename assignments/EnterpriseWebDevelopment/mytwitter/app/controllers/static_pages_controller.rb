class StaticPagesController < ApplicationController
  def home
    @microposts = current_user.microposts if signed_in?
    @micropost = current_user.microposts.build if signed_in?
    if !@movie_review.nil?
    @movie_review = current_user.movie_reviews.build if signed_in?
    end
    @genres = Genre.all if signed_in?
    @mediatypes = Medium.all if signed_in?
    @user = current_user if signed_in?
  end

  def help
  end
  
  def about
  end
end
