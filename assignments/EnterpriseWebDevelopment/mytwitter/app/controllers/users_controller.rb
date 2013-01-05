class UsersController < ApplicationController
  def show
    @user = User.find(params[:id])
    @microposts = @user.microposts    # NEW LINE
    @movie_reviews = @user.movie_reviews    # NEW LINE
  end

  def new
    @user = User.new
  end
  
  def create
    @user = User.new(params[:user])
    if @user.save
      flash[:success] = "Welcome to MovieTweet!"    # NEW LINE
      redirect_to @user
    else
      render 'new'
    end
  end
end