class UsersController < ApplicationController
  def show
    @user = User.find(params[:id])
    @microposts = @user.microposts    # NEW LINE
  end

  def new
    @user = User.new
  end
  
  def create
    @user = User.new(params[:user])
    if @user.save
      flash[:success] = "Welcome to the Sample App!"    # NEW LINE
      redirect_to @user
    else
      render 'new'
    end
  end
end