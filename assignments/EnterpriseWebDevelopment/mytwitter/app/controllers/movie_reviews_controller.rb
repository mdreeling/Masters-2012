class MovieReviewsController < ApplicationController
  before_filter :signed_in_user, only: [:create, :destroy]
  before_filter :correct_user,   only: :destroy    # NEW LINE
  
  def show
    @movie_review = MovieReview.find(params[:id])
    @user = @movie_review.user
  end
  
  def create
    @movie_review = current_user.movie_reviews.build(params[:movie_review])
    if @movie_review.save
      flash[:success] = "Movie Review created!"
      redirect_to root_url
    else
      render 'static_pages/home'
    end
  end
  
  def index
    if current_user.nil?
      @movie_reviews = MovieReview.find(:all)
    else
    @user = current_user
    @movie_reviews = @user.movie_reviews    # NEW LINE
    end
  end
  
  # UPDATED IMPLEMENTATION
  def destroy
    @movie_review.destroy
    redirect_to root_url
  end

  # NEW PRIVATE METHOD
  private

  def correct_user
    @movie_review = current_user.movie_reviews.find_by_id(params[:id])
    redirect_to root_url if @movie_review.nil?
  end
end