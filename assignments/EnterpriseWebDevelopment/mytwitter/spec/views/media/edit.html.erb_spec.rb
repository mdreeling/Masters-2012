require 'spec_helper'

describe "media/edit" do
  before(:each) do
    @medium = assign(:medium, stub_model(Medium,
      :name => "MyString"
    ))
  end

  it "renders the edit medium form" do
    render

    # Run the generator again with the --webrat flag if you want to use webrat matchers
    assert_select "form", :action => media_path(@medium), :method => "post" do
      assert_select "input#medium_name", :name => "medium[name]"
    end
  end
end
